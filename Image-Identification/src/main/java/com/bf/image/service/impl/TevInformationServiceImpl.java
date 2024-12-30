package com.bf.image.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.config.MinIOConfig;
import com.bf.image.domin.vo.DetailInformationVo;
import com.bf.image.entity.DetailInformation;
import com.bf.image.entity.ImageInformation;
import com.bf.image.entity.InspectionWorkOrder;
import com.bf.image.entity.TevInformation;
import com.bf.image.mapper.TevInformationMapper;
import com.bf.image.domin.vo.ChartsVo;
import com.bf.image.domin.SeriesData;
import com.bf.image.domin.vo.TevVo;
import com.bf.image.utils.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author Gplus-033
* @description 针对表【tev_information】的数据库操作Service实现
* @createDate 2024-12-27 12:27:47
*/
@Service
public class TevInformationServiceImpl extends ServiceImpl<TevInformationMapper, TevInformation>
    implements IService<TevInformation> {

    @Autowired
    private TevInformationMapper tevMapper;

    @Autowired
    private MinIOUServiceImpl minIOUService;

    @Autowired
    private ImageInformationServiceImpl imageInformationService;

    @Autowired
    private DetailInformationServiceImpl detailInformationService;

    @Autowired
    private MinIOConfig minIOConfig;

    public List<TevInformation> getList(LambdaQueryWrapper queryWrapper) {
        return tevMapper.selectList(queryWrapper);
    }

    public ChartsVo chartsInfo(List<TevVo> records) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ChartsVo chartsVo = new ChartsVo();
        List<SeriesData> seriesDataList = new ArrayList<>();

        List<String> xList = records.stream()
                .sorted(Comparator.comparing(TevInformation::getCreateTime))
                .map(record -> sdf.format(record.getCreateTime()))
                .collect(Collectors.toList());

        List<Double> quasiPeakValueList = records.stream()
                .sorted(Comparator.comparing(TevInformation::getCreateTime))
                .map(record -> record.getQuasiPeakValue())
                .collect(Collectors.toList());

        List<Double> maxValueList = records.stream()
                .sorted(Comparator.comparing(TevInformation::getCreateTime))
                .map(record -> record.getMaxValue())
                .collect(Collectors.toList());

        List<Double> minValueList = records.stream()
                .sorted(Comparator.comparing(TevInformation::getCreateTime))
                .map(record -> record.getMinValue())
                .collect(Collectors.toList());



        SeriesData quasiPeakValueData = SeriesData.builder()
                .name("局部TEV有效值(V)")
                .data(quasiPeakValueList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(0, quasiPeakValueData);

        SeriesData maxValueData = SeriesData.builder()
                .name("局部TEV最大值(V)")
                .data(maxValueList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(1, maxValueData);

        SeriesData minValueData = SeriesData.builder()
                .name("局部TEV最小值(V)")
                .data(minValueList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(2, minValueData);

        chartsVo.setCategories(xList);
        chartsVo.setSeriesDataList(seriesDataList);

        return chartsVo;
    }

    public IPage<TevVo> pageVo(String queryData) {
        if (StringUtils.isBlank(queryData)) {
            return new Page<>();
        }

        JSONObject queryJsonObj = JSONObject.parseObject(queryData);

        String current = queryJsonObj.get("current").toString();
        String pageSize = queryJsonObj.get("pageSize").toString();

        queryJsonObj.remove("current");
        queryJsonObj.remove("pageSize");

        List<InspectionWorkOrder> inspectionWorkOrderList = detailInformationService.queryForInspectionWorkList(queryJsonObj, 1);

        if (CollectionUtil.isEmpty(inspectionWorkOrderList)) {
            return new Page<>();
        }

        List<Long> TevIdList = inspectionWorkOrderList.stream()
                .map(InspectionWorkOrder::getTevId)
                .collect(Collectors.toList());

        Page<TevInformation> page = tevMapper.selectPage(new Page<>(Long.valueOf(current), Long.valueOf(pageSize)),
                new LambdaQueryWrapper<TevInformation>().in(TevInformation::getTevId, TevIdList));

        List<TevInformation> tevInformationList = page.getRecords();

        List<TevVo> tevVoList = JsonParser.convertToList(tevInformationList, TevVo.class);

        List<Long> imageIdList = tevVoList.stream()
                .filter(tevVo -> Objects.nonNull(tevVo.getImageId()))
                .map(TevInformation::getImageId)
                .collect(Collectors.toList());

        Map<Long, ImageInformation> imageMap = new HashMap<>();

        if (CollectionUtil.isNotEmpty(imageIdList)) {
            List<ImageInformation> imageInformationList = imageInformationService.list(new LambdaQueryWrapper<ImageInformation>()
                    .in(ImageInformation::getImageId, imageIdList));

            imageMap = imageInformationList.stream()
                    .collect(Collectors.toMap(ImageInformation::getImageId, Function.identity()));
        }

        Map<Long, InspectionWorkOrder> tevWorkOrderMap = inspectionWorkOrderList.stream()
                .collect(Collectors.toMap(InspectionWorkOrder::getTevId, Function.identity()));

        for (TevVo tevVo : tevVoList) {
            Long tevId = tevVo.getTevId();

            Long imageId = tevVo.getImageId();

            if (Objects.nonNull(imageId)) {
                ImageInformation imageInformation = imageMap.get(imageId);

                if (Objects.nonNull(imageInformation)) {
                    String storageName = imageInformation.getStorageName();
                    String previewUrl = minIOUService.getPreviewUrl(storageName, minIOConfig.getBucketName());

                    tevVo.setImageInformation(imageInformation);
                    tevVo.setPreviewUrl(previewUrl);
                }
            }

            InspectionWorkOrder inspectionWorkOrder = tevWorkOrderMap.get(tevId);
            tevVo.setTevInspectionWorkOrder(inspectionWorkOrder);
        }

        IPage<TevVo> resultPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());

        resultPage.setRecords(tevVoList);

        return resultPage;
    }
}




