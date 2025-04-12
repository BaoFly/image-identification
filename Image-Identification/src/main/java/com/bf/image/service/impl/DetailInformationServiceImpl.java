package com.bf.image.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.stream.CollectorUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.config.MinIOConfig;
import com.bf.image.domin.vo.FileLatticeDataVo;
import com.bf.image.entity.*;
import com.bf.image.mapper.DetailInformationMapper;
import com.bf.image.domin.vo.ChartsVo;
import com.bf.image.domin.vo.DetailInformationVo;
import com.bf.image.domin.SeriesData;
import com.bf.image.utils.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author Gplus-033
* @description 针对表【detail_information】的数据库操作Service实现
* @createDate 2024-12-27 12:27:39
*/
@Service
public class DetailInformationServiceImpl extends ServiceImpl<DetailInformationMapper, DetailInformation>
    implements IService<DetailInformation> {

    @Autowired
    private DetailInformationMapper detailMapper;

    @Autowired
    private MinIOUServiceImpl minIOUService;

    @Autowired
    private TevInformationServiceImpl tevInformationService;

    @Autowired
    private ImageInformationServiceImpl imageInformationService;

    @Autowired
    private InspectionWorkOrderServiceImpl inspectionWorkOrderService;

    @Autowired
    private MinIOConfig minIOConfig;

    @Autowired
    private FileLatticeDataServiceImpl latticeDataService;

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public List<DetailInformation> getList(LambdaQueryWrapper queryWrapper) {
        return detailMapper.selectList(queryWrapper);
    }

    public ChartsVo chartsInfo(List<DetailInformationVo> records) {
        ChartsVo chartsVo = new ChartsVo();
        List<SeriesData> seriesDataList = new ArrayList<>();

        List<String> xList = records.stream()
                .sorted(Comparator.comparing(DetailInformation::getCreateTime))
                .map(record -> sdf.format(record.getCreateTime()))
                .collect(Collectors.toList());

        List<Double> ambientTempList = records.stream()
                .sorted(Comparator.comparing(DetailInformation::getCreateTime))
                .map(record -> record.getAmbientTemp())
                .collect(Collectors.toList());

        List<Double> centralTempList = records.stream()
                .sorted(Comparator.comparing(DetailInformation::getCreateTime))
                .map(record -> record.getCentralTemp())
                .collect(Collectors.toList());

        List<Double> maxTempList = records.stream()
                .sorted(Comparator.comparing(DetailInformation::getCreateTime))
                .map(record -> record.getMaxTemp())
                .collect(Collectors.toList());

        List<Double> minTempList = records.stream()
                .sorted(Comparator.comparing(DetailInformation::getCreateTime))
                .map(record -> record.getMinTemp())
                .collect(Collectors.toList());


        SeriesData ambient = SeriesData.builder()
                .name("平均温度(°C)")
                .data(ambientTempList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(0, ambient);

        SeriesData cHumidity = SeriesData.builder()
                .name("中心温度(°C)")
                .data(centralTempList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(1, cHumidity);

        SeriesData maxTemp = SeriesData.builder()
                .name("最高温度(°C)")
                .data(maxTempList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(2, maxTemp);

        SeriesData minTemp = SeriesData.builder()
                .name("最低温度(°C)")
                .data(minTempList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(3, minTemp);


        chartsVo.setCategories(xList);
        chartsVo.setSeriesDataList(seriesDataList);

        return chartsVo;
    }

    public IPage<DetailInformationVo> pageVo(String queryData) {

        if (StringUtils.isBlank(queryData)) {
            return new Page<>();
        }

        JSONObject queryJsonObj = JSONObject.parseObject(queryData);

        String current = queryJsonObj.get("current").toString();
        String pageSize = queryJsonObj.get("pageSize").toString();

        queryJsonObj.remove("current");
        queryJsonObj.remove("pageSize");

        List<InspectionWorkOrder> inspectionWorkOrderList = queryForInspectionWorkList(queryJsonObj, 0);

        if (CollectionUtil.isEmpty(inspectionWorkOrderList)) {
            return new Page<>();
        }

        List<Long> detailIdList = inspectionWorkOrderList.stream()
                .map(InspectionWorkOrder::getDetailId)
                .collect(Collectors.toList());

        Page<DetailInformation> page = detailMapper.selectPage(new Page<>(Long.valueOf(current), Long.valueOf(pageSize)),
                new LambdaQueryWrapper<DetailInformation>().in(DetailInformation::getDetailId, detailIdList));

        List<DetailInformation> detailInformationList = page.getRecords();

        List<DetailInformationVo> detailInformationVoList = JsonParser.convertToList(detailInformationList, DetailInformationVo.class);

        // 如果有则拿到点阵数据
        List<FileLatticeData> fileLatticeDataList = latticeDataService.list(
                Wrappers.lambdaQuery(FileLatticeData.class)
                        .in(FileLatticeData::getDetailId, detailIdList));

        List<Long> imageIdList = detailInformationVoList.stream()
                .filter(detailInformationVo -> Objects.nonNull(detailInformationVo.getImageId()))
                .map(DetailInformation::getImageId)
                .collect(Collectors.toList());

        Map<Long, ImageInformation> imageMap = new HashMap<>();

        if (CollectionUtil.isNotEmpty(imageIdList)) {
            List<ImageInformation> imageInformationList = imageInformationService.list(new LambdaQueryWrapper<ImageInformation>()
                    .in(ImageInformation::getImageId, imageIdList));

            imageMap = imageInformationList.stream()
                    .collect(Collectors.toMap(ImageInformation::getImageId, Function.identity()));
        }

        Map<Long, InspectionWorkOrder> detailWorkOrderMap = inspectionWorkOrderList.stream()
                .collect(Collectors.toMap(InspectionWorkOrder::getDetailId, Function.identity()));

        for (DetailInformationVo detailInformationVo : detailInformationVoList) {
            Long detailId = detailInformationVo.getDetailId();

            InspectionWorkOrder inspectionWorkOrder = detailWorkOrderMap.get(detailId);

            Long imageId = detailInformationVo.getImageId();

            if (Objects.nonNull(imageId)) {
                ImageInformation imageInformation = imageMap.get(imageId);

                if (Objects.nonNull(imageInformation)) {
                    String storageName = imageInformation.getStorageName();
                    String previewUrl = minIOUService.getPreviewUrl(storageName, minIOConfig.getBucketName());

                    detailInformationVo.setImageInformation(imageInformation);
                    detailInformationVo.setPreviewUrl(previewUrl);
                }
            }

            // 塞点阵数据
            if (CollectionUtil.isNotEmpty(fileLatticeDataList)) {
                List<FileLatticeData> latticeDataList = fileLatticeDataList.stream()
                        .filter(fileLatticeData -> detailId.equals(fileLatticeData.getDetailId()))
                        .collect(Collectors.toList());

                if (CollectionUtil.isNotEmpty(latticeDataList)) {
                    List<FileLatticeDataVo> fileLatticeDataVoList = JsonParser.convertToList(latticeDataList, FileLatticeDataVo.class);

                    for (FileLatticeDataVo fileLatticeDataVo : fileLatticeDataVoList) {
                        String fileStorageName = fileLatticeDataVo.getFileStorageName();

                        String previewUrl = minIOUService.getPreviewUrl(fileStorageName, minIOConfig.getBucketName());

                        fileLatticeDataVo.setPreviewUrl(previewUrl);
                    }

                    detailInformationVo.setFileLatticeDataVoList(fileLatticeDataVoList);
                }
            }

            detailInformationVo.setDetailInspectionWorkOrder(inspectionWorkOrder);
        }

        IPage<DetailInformationVo> resultPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());

        resultPage.setRecords(detailInformationVoList);

        return resultPage;
    }

    public List<InspectionWorkOrder> queryForInspectionWorkList(JSONObject queryJsonObj,
                                                                 Integer type) {

        LambdaQueryWrapper<InspectionWorkOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InspectionWorkOrder::getType, type);

        List<InspectionWorkOrder> inspectionWorkOrderList = new ArrayList<>();

        if (queryJsonObj.isEmpty()) {
            inspectionWorkOrderList = inspectionWorkOrderService.list(queryWrapper);
        } else {
            Object businessId = queryJsonObj.get("businessId");
            Object dispatchName = queryJsonObj.get("dispatchName");
            Object userName = queryJsonObj.get("userName");
            Object inspectionStatus = queryJsonObj.get("inspectionStatus");
            Object deviceName = queryJsonObj.get("deviceName");
            Object inspectionTeam = queryJsonObj.get("inspectionTeam");
            Object feeder = queryJsonObj.get("feeder");
            Object powerSupplyStation = queryJsonObj.get("powerSupplyStation");
            Object deviceType = queryJsonObj.get("deviceType");
            Object distributionRoomName = queryJsonObj.get("distributionRoomName");
            Object planStartTime = queryJsonObj.get("planStartTime");
            Object updateTime = queryJsonObj.get("updateTime");

            List<Long> dataIdList = new ArrayList<>();

            if (type == 0) {
                LambdaQueryWrapper<DetailInformation> dataInformationLambdaQueryWrapper = new LambdaQueryWrapper<DetailInformation>();

                if (powerSupplyStation != null) {
                    dataInformationLambdaQueryWrapper.eq(DetailInformation::getPowerSupplyStation, Integer.valueOf(powerSupplyStation.toString()));
                }

                if (deviceType != null) {
                    dataInformationLambdaQueryWrapper.eq(DetailInformation::getDeviceType, Integer.valueOf(deviceType.toString()));
                }

                if (distributionRoomName != null) {
                    dataInformationLambdaQueryWrapper.eq(DetailInformation::getDistributionRoomName, Integer.valueOf(distributionRoomName.toString()));
                }

                List<DetailInformation> dataInformationList = detailMapper.selectList(dataInformationLambdaQueryWrapper);

                dataIdList = dataInformationList.stream()
                        .map(DetailInformation::getDetailId)
                        .collect(Collectors.toList());
            } else if (type == 1) {
                LambdaQueryWrapper<TevInformation> dataInformationLambdaQueryWrapper = new LambdaQueryWrapper<TevInformation>();

                if (powerSupplyStation != null) {
                    dataInformationLambdaQueryWrapper.eq(TevInformation::getPowerSupplyStation, Integer.valueOf(powerSupplyStation.toString()));
                }

                if (deviceType != null) {
                    dataInformationLambdaQueryWrapper.eq(TevInformation::getDeviceType, Integer.valueOf(deviceType.toString()));
                }

                if (distributionRoomName != null) {
                    dataInformationLambdaQueryWrapper.eq(TevInformation::getDistributionRoomName, Integer.valueOf(distributionRoomName.toString()));
                }

                List<TevInformation> dataInformationList = tevInformationService.list(dataInformationLambdaQueryWrapper);

                dataIdList = dataInformationList.stream()
                        .map(TevInformation::getTevId)
                        .collect(Collectors.toList());
            }

            LambdaQueryWrapper<InspectionWorkOrder> workOrderQueryWrapper = new LambdaQueryWrapper<>();
            workOrderQueryWrapper.eq(InspectionWorkOrder::getType, type);

            if (CollectionUtil.isEmpty(dataIdList)) {
                return inspectionWorkOrderList;
            }

            if (type == 0) {
                workOrderQueryWrapper.in(InspectionWorkOrder::getDetailId, dataIdList);
            } else if (type == 1) {
                workOrderQueryWrapper.in(InspectionWorkOrder::getTevId, dataIdList);
            }

            if (businessId != null) {
                workOrderQueryWrapper.eq(InspectionWorkOrder::getInspectionWorkOrderId, businessId.toString());
            }

            if (dispatchName != null) {
                workOrderQueryWrapper.like(InspectionWorkOrder::getDispatchName, dispatchName.toString());
            }

            if (userName != null) {
                workOrderQueryWrapper.like(InspectionWorkOrder::getUserName, userName);
            }

            if (inspectionStatus != null) {
                workOrderQueryWrapper.eq(InspectionWorkOrder::getInspectionStatus, Integer.valueOf(inspectionStatus.toString()));
            }

            if (deviceName != null) {
                workOrderQueryWrapper.eq(InspectionWorkOrder::getDeviceName, Integer.valueOf(deviceName.toString()));
            }

            if (inspectionTeam != null) {
                workOrderQueryWrapper.eq(InspectionWorkOrder::getInspectionTeam, Integer.valueOf(inspectionTeam.toString()));
            }

            if (feeder != null) {
                workOrderQueryWrapper.eq(InspectionWorkOrder::getFeeder, Integer.valueOf(feeder.toString()));
            }

            if (Objects.nonNull(planStartTime)) {
                JSONArray planStartTimeArray = JSONObject.parseArray(planStartTime.toString());

                String startTime = planStartTimeArray.get(0).toString();
                String endTime = planStartTimeArray.get(1).toString();

                workOrderQueryWrapper.between(InspectionWorkOrder::getPlanStartTime, startTime, endTime);
            }

            if (Objects.nonNull(updateTime)) {
                JSONArray updateTimeArray = JSONObject.parseArray(updateTime.toString());

                String startTime = updateTimeArray.get(0).toString();
                String endTime = updateTimeArray.get(1).toString();

                workOrderQueryWrapper.between(InspectionWorkOrder::getPlanStartTime, startTime, endTime);
            }

            inspectionWorkOrderList = inspectionWorkOrderService.list(workOrderQueryWrapper);

        }

        return inspectionWorkOrderList;
    }
}




