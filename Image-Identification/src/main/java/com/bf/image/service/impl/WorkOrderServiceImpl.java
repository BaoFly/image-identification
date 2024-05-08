package com.bf.image.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.config.MinIOConfig;
import com.bf.image.exception.CustomException;
import com.bf.image.pojo.*;
import com.bf.image.service.*;
import com.bf.image.mapper.WorkOrderMapper;
import com.bf.image.vo.WorkOrderVo;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.bf.image.utils.JsonParser.jsonParser;

/**
* @author Gplus-033
* @description 针对表【work_order】的数据库操作Service实现
* @createDate 2024-04-12 09:36:10
*/
@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder>
    implements WorkOrderService{

    @Autowired
    private UserInformationService userService;

    @Autowired
    private DetailInformationService detailService;

    @Autowired
    private TevInformationService tevService;

    @Autowired
    private DeviceInformationService deviceService;

    @Autowired
    private ImageInformationService imageService;

    @Autowired
    private MinIOUService minIOUService;

    @Autowired
    private MinIOConfig minIOConfig;

    @Override
    public IPage<WorkOrderVo> pageVo(WorkOrderVo workOrderVo) {
        Integer pageSize = workOrderVo.getPageSize();
        Integer offset = (workOrderVo.getCurrent() - 1) * pageSize;
        Page<WorkOrder> newPage = new Page<>();
        newPage.setCurrent(offset);
        newPage.setSize(pageSize);

        List<UserInformation> userList = userService.list(Wrappers.lambdaQuery(UserInformation.class)
                .like(StringUtils.isNotBlank(workOrderVo.getUsername()), UserInformation::getUsername, workOrderVo.getUsername()));
        List<UserInformation> leaderList = userService.list(Wrappers.lambdaQuery(UserInformation.class)
                .like(StringUtils.isNotBlank(workOrderVo.getReviewName()), UserInformation::getUsername, workOrderVo.getReviewName()));

        List<Long> userIdList = userList.stream().map(user -> user.getUserId()).collect(Collectors.toList());
        List<Long> leaderIdList = leaderList.stream().map(user -> user.getUserId()).collect(Collectors.toList());

        List<UserInformation> allUserList = userService.list();
        Map<Long, String> userIdNameMap = allUserList.stream().collect(Collectors.toMap(
                user -> user.getUserId(),
                user -> user.getUsername()
        ));

        Date startTime = null;
        Date endTime = null;

        if (workOrderVo.getDateRange() != null && CollectionUtils.isNotEmpty(workOrderVo.getDateRange())) {
            startTime = workOrderVo.getDateRange().get(0);
            endTime = workOrderVo.getDateRange().get(1);
        }

        boolean flag = false;
        if (Objects.isNull(startTime) && CollectionUtils.isEmpty(userIdList) && Objects.isNull(workOrderVo.getStatus())) {
            flag = true;
        }

        Page<WorkOrder> page = this.page(newPage,
                Wrappers.lambdaQuery(WorkOrder.class)
                        .in(CollectionUtils.isNotEmpty(userIdList), WorkOrder::getCreatorId, userIdList)
                        .in(CollectionUtils.isNotEmpty(userIdList), WorkOrder::getReviewerId, leaderIdList)
                        .eq(Objects.nonNull(workOrderVo.getStatus()), WorkOrder::getStatus, workOrderVo.getStatus())
                        .eq(Objects.nonNull(workOrderVo.getType()), WorkOrder::getType, workOrderVo.getType())
                        .eq(Objects.nonNull(workOrderVo.getDetailId()), WorkOrder::getDetailId, workOrderVo.getDetailId())
                        .between(Objects.nonNull(startTime), WorkOrder::getCreateTime, startTime, endTime)
                        .orderByDesc(WorkOrder::getCreateTime));
        CollectionType listType = jsonParser.getTypeFactory().constructCollectionType(ArrayList.class, WorkOrderVo.class);
        List<WorkOrderVo> list = jsonParser.convertValue(page.getRecords(), listType);
        Page<WorkOrderVo> objectPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());

        if (CollectionUtils.isEmpty(list)) {
            return objectPage;
        }

        list.forEach(vo -> {
            Long userId = vo.getCreatorId();
            Long reviewerId = vo.getReviewerId();

            vo.setUsername(userIdNameMap.get(userId));
            vo.setReviewName(userIdNameMap.get(reviewerId));
        });

        objectPage.setRecords(list);

        return objectPage;
    }

    @Override
    public WorkOrderVo detail(WorkOrderVo workOrderVo) {
        WorkOrder workOrder = this.getById(workOrderVo.getWorkOrderId());
        workOrderVo = jsonParser.convertValue(workOrder, WorkOrderVo.class);
        Integer type = workOrderVo.getType();
        Long detailId = workOrderVo.getDetailId();

        Object bizObj = null;

        Long deviceId = null;
        Long imageId = null;
        ImageInformation image = null;
        DeviceInformation device = null;

        List<UserInformation> allUserList = userService.list();
        Map<Long, String> userIdNameMap = allUserList.stream().collect(Collectors.toMap(
                user -> user.getUserId(),
                user -> user.getUsername()
        ));


        if (type == 1) {
            DetailInformation detailInformation = detailService.getDetailById(detailId);
            deviceId = detailInformation.getDevice().getDeviceId();
            imageId = detailInformation.getImage().getImageId();
            bizObj = detailInformation;
        } else if (type == 2) {
            TevInformation tevInformation = tevService.getById(detailId);
            deviceId = tevInformation.getDeviceId();
            imageId = tevInformation.getImageId();
            bizObj = tevInformation;
        }

        device = deviceService.getById(deviceId);
        image = imageService.getById(imageId);
        String previewUrl = minIOUService.getPreviewUrl(image.getStorageName(), minIOConfig.getBucketName());

        workOrderVo.setBizObj(bizObj);
        workOrderVo.setDevice(device);
        workOrderVo.setImage(image);
        workOrderVo.setPreviewUrl(previewUrl);
        workOrderVo.setUsername(userIdNameMap.get(workOrderVo.getCreatorId()));
        workOrderVo.setReviewName(userIdNameMap.get(workOrderVo.getReviewerId()));

        return workOrderVo;
    }

    @Override
    public void create(WorkOrderVo workOrderVo) {
        Long bizId = workOrderVo.getDetailId();

        if (Objects.nonNull(this.getOne(Wrappers.lambdaQuery(WorkOrder.class).eq(WorkOrder::getDetailId, bizId)))) {
            throw new CustomException("该记录已审核过，请勿重复提交");
        }

        WorkOrder workOrder = null;
        if (workOrderVo.getType() == 1) {
            String username = workOrderVo.getUsername();
            String reviewName = workOrderVo.getReviewName();

            UserInformation user = userService.getOne(Wrappers.lambdaQuery(UserInformation.class)
                    .eq(UserInformation::getUsername, username));
            UserInformation reviewer = userService.getOne(Wrappers.lambdaQuery(UserInformation.class)
                    .eq(UserInformation::getUsername, reviewName));

            workOrderVo.setUsername(null);
            workOrderVo.setReviewName(null);

            workOrder = jsonParser.convertValue(workOrderVo, WorkOrder.class);

            workOrder.setCreatorId(user.getUserId());
            workOrder.setReviewerId(reviewer.getUserId());
        } else {
            workOrder = jsonParser.convertValue(workOrderVo, WorkOrder.class);
        }
        workOrder.setStatus(2);
        this.save(workOrder);
    }

    @Override
    public void review(WorkOrderVo workOrderVo) {
        Long workOrderId = workOrderVo.getWorkOrderId();
        WorkOrder order = this.getById(workOrderId);
        if (Objects.isNull(order)) {
            throw new CustomException("无该工单，请联系人工客服");
        }

        Integer status = workOrderVo.getStatus();
        if (status == 3) {
            workOrderVo.setReviewTime(new Date());
            workOrderVo.setCompleteTime(new Date());
        } else if (status == 4) {
            workOrderVo.setReviewTime(new Date());
            workOrderVo.setFailTime(new Date());
        } else {
            throw new CustomException("状态不对，请勿乱更新");
        }
        WorkOrder workOrder = jsonParser.convertValue(workOrderVo, WorkOrder.class);
        this.updateById(workOrder);
    }
}




