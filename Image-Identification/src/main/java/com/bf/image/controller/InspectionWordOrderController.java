package com.bf.image.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.func.Func;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.bf.image.common.BaseException;
import com.bf.image.domin.vo.InspectionWorkOrderVo;
import com.bf.image.entity.DetailInformation;
import com.bf.image.entity.InspectionWorkOrder;
import com.bf.image.entity.TevInformation;
import com.bf.image.exception.CustomException;
import com.bf.image.service.impl.DetailInformationServiceImpl;
import com.bf.image.service.impl.InspectionWorkOrderServiceImpl;
import com.bf.image.domin.ResultJson;
import com.bf.image.service.impl.TevInformationServiceImpl;
import com.bf.image.utils.JsonParser;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Api(tags = "巡检工单相关接口")
@RestController
@CrossOrigin
public class InspectionWordOrderController {

    @Autowired
    private DetailInformationServiceImpl detailInformationService;

    @Autowired
    private InspectionWorkOrderServiceImpl inspectionWorkOrderService;

    @Autowired
    private TevInformationServiceImpl tevInformationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT);

    @PostMapping("inspectionWorkOrder/updateData")
    @Transactional
    public ResultJson updateData(@RequestBody String data) {
        if (StringUtils.isBlank(data)) {
            return ResultJson.fail("更新的数据不能为空");
        }

        JSONObject json = JSONObject.parseObject(data);

        String businessId = json.get("businessId").toString();
        String inspectionStatus = json.get("inspectionStatus").toString();
        String deviceType = json.get("deviceType").toString();
        String imageId = json.get("imageId").toString();
        String powerSupplyStation = json.get("powerSupplyStation").toString();
        String distributionRoomName = json.get("distributionRoomName").toString();

        InspectionWorkOrder inspectionWorkOrder = inspectionWorkOrderService.getById(businessId);

        Integer type = inspectionWorkOrder.getType();

        if (type == 0) {
            Long detailId = inspectionWorkOrder.getDetailId();

            String ambientTemp = json.get("ambientTemp").toString();
            String centralTemp = json.get("centralTemp").toString();
            String maxTemp = json.get("maxTemp").toString();
            String minTemp = json.get("minTemp").toString();
            String inspectionDetail = json.get("inspectionDetail").toString();

            DetailInformation detailInformation = DetailInformation.builder()
                    .detailId(detailId)
                    .imageId(Long.valueOf(imageId))
                    .deviceType(Integer.valueOf(deviceType))
                    .powerSupplyStation(Integer.valueOf(powerSupplyStation))
                    .distributionRoomName(Integer.valueOf(distributionRoomName))
                    .ambientTemp(Double.valueOf(ambientTemp))
                    .centralTemp(Double.valueOf(centralTemp))
                    .maxTemp(Double.valueOf(maxTemp))
                    .minTemp(Double.valueOf(minTemp))
                    .inspectionDetail(inspectionDetail)
                    .updateTime(new Date())
                    .build();

            boolean flag = detailInformationService.updateById(detailInformation);

            if (!flag) {
                throw new CustomException("更新红外图像信息出现异常");
            }
        } else if (type == 1) {
            Long tevId = inspectionWorkOrder.getTevId();

            String quasiPeakValue = json.get("quasiPeakValue").toString();
            String maxValue = json.get("maxValue").toString();
            String minValue = json.get("minValue").toString();

            TevInformation tevInformation = TevInformation.builder()
                    .tevId(tevId)
                    .imageId(Long.valueOf(imageId))
                    .deviceType(Integer.valueOf(deviceType))
                    .powerSupplyStation(Integer.valueOf(powerSupplyStation))
                    .distributionRoomName(Integer.valueOf(distributionRoomName))
                    .quasiPeakValue(Double.valueOf(quasiPeakValue))
                    .maxValue(Double.valueOf(maxValue))
                    .minValue(Double.valueOf(minValue))
                    .inspectionDetail(inspectionStatus)
                    .updateTime(new Date())
                    .build();

            boolean flag = tevInformationService.updateById(tevInformation);

            if (!flag) {
                throw new CustomException("更新局放数据信息出现异常");
            }
        }

        InspectionWorkOrder update = InspectionWorkOrder.builder()
                .inspectionWorkOrderId(Long.valueOf(businessId))
                .inspectionStatus(Integer.valueOf(inspectionStatus))
                .updateTime(new Date())
                .build();

        boolean flag = inspectionWorkOrderService.updateById(update);

        if (!flag) {
            throw new CustomException("更新工单数据出现异常");
        }

        return ResultJson.success("更新成功");

    }

    @PostMapping("inspectionWorkOrder/create")
    @Transactional
    public ResultJson create(@RequestBody String data) throws ParseException {
        if (StringUtils.isBlank(data)) {
            return ResultJson.fail("参数不能为空");
        }

        JSONObject json = JSONObject.parseObject(data);

        String businessId = json.get("businessId").toString();
        String dispatchName = json.get("dispatchName").toString();
        String inspectionTeam = json.get("inspectionTeam").toString();
        String type = json.get("type").toString();
        String userName = json.get("userName").toString();
        String deviceName = json.get("deviceName").toString();
        String feeder = json.get("feeder").toString();
        String planStartTime = json.get("planStartTime").toString();
        String planEndTime = json.get("planEndTime").toString();

        Long dataId = 0L;

        if ("0".equals(type)) {
            DetailInformation detail = DetailInformation.builder()
                    .ambientTemp(0.0)
                    .centralTemp(0.0)
                    .maxTemp(0.0)
                    .minTemp(0.0)
                    .build();

            boolean save = detailInformationService.save(detail);

            if (!save) {
                throw new CustomException("插入红外数据出现异常");
            }

            dataId = detail.getDetailId();
        } else if ("1".equals(type)) {
            TevInformation tevInformation = TevInformation.builder()
                    .quasiPeakValue(0.0)
                    .maxValue(0.0)
                    .minValue(0.0)
                    .build();

            boolean save = tevInformationService.save(tevInformation);

            if (!save) {
                throw new CustomException("插入局放数据出现异常");
            }

            dataId = tevInformation.getTevId();
        }

        InspectionWorkOrder inspectionWorkOrder = InspectionWorkOrder.builder()
                .inspectionWorkOrderId(Long.valueOf(businessId))
                .dispatchName(dispatchName)
                .userName(userName)
                .deviceName(Integer.valueOf(deviceName))
                .inspectionTeam(Integer.valueOf(inspectionTeam))
                .feeder(Integer.valueOf(feeder))
                .type(Integer.valueOf(type))
                .planStartTime(DateUtils.parseDate(planStartTime, FORMAT))
                .planEndTime(DateUtils.parseDate(planEndTime, FORMAT))
                .build();

        if (Integer.valueOf(type) == 0) {
            inspectionWorkOrder.setDetailId(dataId);
        } else {
            inspectionWorkOrder.setTevId(dataId);
        }

        boolean save = inspectionWorkOrderService.save(inspectionWorkOrder);

        if (!save) {
            throw new CustomException("创建工单出现异常");
        }

        return ResultJson.success("创建工单成功");
    }

    @GetMapping("inspectionWorkOrder/query")
    public ResultJson query(String userName) {
        if (StringUtils.isBlank(userName)) {
            return ResultJson.fail("用户名不能为空");
        }

        LambdaQueryWrapper<InspectionWorkOrder> queryWrapper = new LambdaQueryWrapper<InspectionWorkOrder>()
                .eq(InspectionWorkOrder::getUserName, userName)
                .eq(InspectionWorkOrder::getInspectionStatus, 0);

        List<InspectionWorkOrder> inspectionWorkOrderList = inspectionWorkOrderService.getList(queryWrapper);

        if (CollectionUtil.isEmpty(inspectionWorkOrderList)) {
            return ResultJson.success();
        }

        List<InspectionWorkOrderVo> inspectionWorkOrderVoList = new ArrayList<>();

        List<Long> detailIdList = inspectionWorkOrderList.stream()
                .filter(workOrder -> workOrder.getType() == 0)
                .map(InspectionWorkOrder::getDetailId)
                .collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(detailIdList)) {
            List<DetailInformation> detailInformationList = detailInformationService.getList(new LambdaQueryWrapper<DetailInformation>()
                    .in(DetailInformation::getDetailId, detailIdList));

            List<InspectionWorkOrder> detailInspectionWorkOrderList = inspectionWorkOrderList.stream()
                    .filter(workOrder -> detailIdList.contains(workOrder.getDetailId()))
                    .collect(Collectors.toList());

            Map<Long, InspectionWorkOrder> detailWorkOrderMap = detailInspectionWorkOrderList.stream()
                    .collect(Collectors.toMap(
                            InspectionWorkOrder::getDetailId,
                            Function.identity()
                    ));

            for (DetailInformation detailInformation : detailInformationList) {
                Long detailId = detailInformation.getDetailId();

                InspectionWorkOrder inspectionWorkOrder = detailWorkOrderMap.get(detailId);

                InspectionWorkOrderVo inspectionWorkOrderVo = JsonParser.convertValue(inspectionWorkOrder, InspectionWorkOrderVo.class);

                inspectionWorkOrderVo.setData(detailInformation);

                inspectionWorkOrderVoList.add(inspectionWorkOrderVo);
            }

        }

        List<Long> tevIdList = inspectionWorkOrderList.stream()
                .filter(workOrder -> workOrder.getType() == 1)
                .map(InspectionWorkOrder::getTevId)
                .collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(tevIdList)) {
            List<TevInformation> tevInformationList = tevInformationService.getList(new LambdaQueryWrapper<TevInformation>()
                    .in(TevInformation::getTevId, tevIdList));

            List<InspectionWorkOrder> tevWorkOrderList = inspectionWorkOrderList.stream()
                    .filter(workOrder -> tevIdList.contains(workOrder.getTevId()))
                    .collect(Collectors.toList());

            Map<Long, InspectionWorkOrder> tevWorkOrderMap = tevWorkOrderList.stream()
                    .collect(Collectors.toMap(
                            InspectionWorkOrder::getTevId,
                            Function.identity()
                    ));

            for (TevInformation tevInformation : tevInformationList) {
                Long tevId = tevInformation.getTevId();

                InspectionWorkOrder inspectionWorkOrder = tevWorkOrderMap.get(tevId);

                InspectionWorkOrderVo inspectionWorkOrderVo = JsonParser.convertValue(inspectionWorkOrder, InspectionWorkOrderVo.class);

                inspectionWorkOrderVo.setData(tevInformation);

                inspectionWorkOrderVoList.add(inspectionWorkOrderVo);
            }
        }

        return ResultJson.success(inspectionWorkOrderVoList);
    }


}
