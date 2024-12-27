package com.bf.image.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.config.MinIOConfig;
import com.bf.image.entity.InspectionWorkOrder;
import com.bf.image.entity.TevInformation;
import com.bf.image.mapper.TevInformationMapper;
import com.bf.image.domin.vo.ChartsVo;
import com.bf.image.domin.SeriesData;
import com.bf.image.domin.vo.TevVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
    private UserInformationServiceImpl userService;


    @Autowired
    private MinIOUServiceImpl minIOUService;

    @Autowired
    private ImageInformationServiceImpl imageService;

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
                .name("局部TEV有效值(mV)")
                .data(quasiPeakValueList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(0, quasiPeakValueData);

        SeriesData maxValueData = SeriesData.builder()
                .name("局部TEV最大值(mV)")
                .data(maxValueList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(1, maxValueData);

        SeriesData minValueData = SeriesData.builder()
                .name("局部TEV最小值(mV)")
                .data(minValueList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(2, minValueData);

        chartsVo.setCategories(xList);
        chartsVo.setSeriesDataList(seriesDataList);

        return chartsVo;
    }

    public IPage<TevVo> pageVo(TevVo tevVo) {
        return null;
    }
}




