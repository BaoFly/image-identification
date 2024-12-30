package com.bf.image.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.func.Func;
import cn.hutool.core.stream.CollectorUtil;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @PostMapping("inspectionWorkOrder/truncateAllTable")
    @Transactional
    public ResultJson truncateAllTable() {
        List<TevInformation> tevInformationList = tevInformationService.list();

        List<DetailInformation> detailInformationList = detailInformationService.list();

        List<InspectionWorkOrder> inspectionWorkOrderList = inspectionWorkOrderService.list();

        List<Long> detailIdList = detailInformationList.stream()
                .map(DetailInformation::getDetailId)
                .collect(Collectors.toList());

        List<Long> tevIdList = tevInformationList.stream()
                .map(TevInformation::getTevId)
                .collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(detailIdList)) {
            boolean flag = detailInformationService.removeByIds(detailIdList);

            if (!flag) {
                return ResultJson.fail("删除红外数据出现异常");
            }
        }

        if (CollectionUtil.isNotEmpty(tevIdList)) {
            boolean flag = tevInformationService.removeByIds(tevIdList);

            if (!flag) {
                return ResultJson.fail("删除局放数据出现异常");
            }
        }

        if (CollectionUtil.isNotEmpty(inspectionWorkOrderList)) {
            List<Long> inspectionWorkOrderIdList = inspectionWorkOrderList.stream()
                    .map(InspectionWorkOrder::getInspectionWorkOrderId)
                    .collect(Collectors.toList());

            boolean flag = inspectionWorkOrderService.removeByIds(inspectionWorkOrderIdList);

            if (!flag) {
                return ResultJson.fail("删除工单数据出现异常");
            }
        }

        return ResultJson.success("清空所有表成功");
    }

    @PostMapping("inspectionWorkOrder/deleteById")
    @Transactional
    public ResultJson truncateAllTable(Long businessId) {
        if (Objects.isNull(businessId)) {
            return ResultJson.fail("必要参数不能为空");
        }

        InspectionWorkOrder inspectionWorkOrder = inspectionWorkOrderService.getById(businessId);

        if (Objects.isNull(inspectionWorkOrder)) {
            return ResultJson.fail("无对应的工单记录");
        }

        Integer type = inspectionWorkOrder.getType();

        if (type == 0) {
            Long detailId = inspectionWorkOrder.getDetailId();

            detailInformationService.removeById(detailId);
        } else {
            Long tevId = inspectionWorkOrder.getTevId();

            tevInformationService.removeById(tevId);
        }

        inspectionWorkOrderService.removeById(inspectionWorkOrder.getInspectionWorkOrderId());

        return ResultJson.success("删除对应工单及其相关记录成功");
    }

    @PostMapping("inspectionWorkOrder/updateData")
    @Transactional
    public ResultJson updateData(@RequestBody String data) {
        if (StringUtils.isBlank(data)) {
            return ResultJson.fail("更新的数据不能为空");
        }

        JSONObject json = JSONObject.parseObject(data);

        String businessId = null;
        String inspectionStatus = null;
        String deviceType = null;
        String imageId = null;
        String powerSupplyStation = null;
        String distributionRoomName = null;
        try {
            businessId = json.get("businessId").toString();
            inspectionStatus = json.get("inspectionStatus").toString();
            deviceType = json.get("deviceType").toString();
            imageId = json.get("imageId").toString();
            powerSupplyStation = json.get("powerSupplyStation").toString();
            distributionRoomName = json.get("distributionRoomName").toString();
        } catch (Exception e) {
            return ResultJson.fail("必要参数不能为空");
        }

        InspectionWorkOrder inspectionWorkOrder = inspectionWorkOrderService.getById(businessId);

        Integer type = inspectionWorkOrder.getType();

        if (type == 0) {
            Long detailId = inspectionWorkOrder.getDetailId();

            String ambientTemp = null;
            String centralTemp = null;
            String maxTemp = null;
            String minTemp = null;
            String inspectionDetail = null;
            try {
                ambientTemp = json.get("ambientTemp").toString();
                centralTemp = json.get("centralTemp").toString();
                maxTemp = json.get("maxTemp").toString();
                minTemp = json.get("minTemp").toString();
                inspectionDetail = json.get("inspectionDetail").toString();
            } catch (Exception e) {
                return ResultJson.fail("必要参数不能为空");
            }

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
                return ResultJson.fail("更新红外图像信息出现异常");
            }
        } else if (type == 1) {
            Long tevId = inspectionWorkOrder.getTevId();

            String quasiPeakValue = json.get("quasiPeakValue").toString();
            String maxValue = json.get("maxValue").toString();
            String minValue = json.get("minValue").toString();
            String inspectionDetail = json.get("inspectionDetail").toString();

            TevInformation tevInformation = TevInformation.builder()
                    .tevId(tevId)
                    .imageId(Long.valueOf(imageId))
                    .deviceType(Integer.valueOf(deviceType))
                    .powerSupplyStation(Integer.valueOf(powerSupplyStation))
                    .distributionRoomName(Integer.valueOf(distributionRoomName))
                    .quasiPeakValue(Double.valueOf(quasiPeakValue))
                    .maxValue(Double.valueOf(maxValue))
                    .minValue(Double.valueOf(minValue))
                    .inspectionDetail(inspectionDetail)
                    .updateTime(new Date())
                    .build();

            boolean flag = tevInformationService.updateById(tevInformation);

            if (!flag) {
                return ResultJson.fail("更新局放数据信息出现异常");
            }
        }

        InspectionWorkOrder update = InspectionWorkOrder.builder()
                .inspectionWorkOrderId(Long.valueOf(businessId))
                .inspectionStatus(Integer.valueOf(inspectionStatus))
                .updateTime(new Date())
                .build();

        boolean flag = inspectionWorkOrderService.updateById(update);

        if (!flag) {
            return ResultJson.fail("更新工单数据出现异常");
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

        String businessId = null;
        String dispatchName = null;
        String inspectionTeam = null;
        String type = null;
        String userName = null;
        String deviceName = null;
        String feeder = null;
        String planStartTime = null;
        String planEndTime = null;
        try {
            businessId = json.get("businessId").toString();
            dispatchName = json.get("dispatchName").toString();
            inspectionTeam = json.get("inspectionTeam").toString();
            type = json.get("type").toString();
            userName = json.get("userName").toString();
            deviceName = json.get("deviceName").toString();
            feeder = json.get("feeder").toString();
            planStartTime = json.get("planStartTime").toString();
            planEndTime = json.get("planEndTime").toString();
        } catch (Exception e) {
            return ResultJson.fail("必要参数不能为空");
        }

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
                return ResultJson.fail("插入红外数据出现异常");
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
                return ResultJson.fail("插入局放数据出现异常");
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
            return ResultJson.fail("创建工单出现异常");
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
