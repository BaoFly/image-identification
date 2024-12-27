package com.bf.image.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.entity.DetailInformation;
import com.bf.image.mapper.DetailInformationMapper;
import com.bf.image.domin.vo.ChartsVo;
import com.bf.image.domin.vo.DetailInformationVo;
import com.bf.image.domin.SeriesData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
    private UserInformationServiceImpl userService;

    @Autowired
    private DetailInformationMapper detailMapper;

    @Autowired
    private MinIOUServiceImpl minIOUService;

    public List<DetailInformation> getList(LambdaQueryWrapper queryWrapper) {
        return detailMapper.selectList(queryWrapper);
    }

    public ChartsVo chartsInfo(List<DetailInformationVo> records) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                .name("环境温度(°C)")
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

    public IPage<DetailInformationVo> pageVo(DetailInformationVo detailInformationVo) {
        return null;
    }
}




