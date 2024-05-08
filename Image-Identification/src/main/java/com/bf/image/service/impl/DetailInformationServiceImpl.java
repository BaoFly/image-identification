package com.bf.image.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.constant.CommonConstant;
import com.bf.image.exception.CustomException;
import com.bf.image.mapper.DeviceInformationMapper;
import com.bf.image.mapper.ImageInformationMapper;
import com.bf.image.mapper.UserInformationMapper;
import com.bf.image.pojo.*;
import com.bf.image.service.*;
import com.bf.image.mapper.DetailInformationMapper;
import com.bf.image.utils.TimeUtil;
import com.bf.image.utils.UUIDUtil;
import com.bf.image.vo.ChartsVo;
import com.bf.image.vo.DetailInformationVo;
import com.bf.image.vo.SeriesData;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【deatil_information】的数据库操作Service实现
* @createDate 2023-10-14 18:40:13
*/
@Service
public class DetailInformationServiceImpl extends ServiceImpl<DetailInformationMapper, DetailInformation>
    implements DetailInformationService {

    @Autowired
    private UserInformationService userService;

    @Autowired
    private DeviceInformationService deviceService;

    @Autowired
    private DetailInformationMapper detailMapper;

    @Autowired
    private MinIOUService minIOUService;

    @Autowired
    private WorkOrderService workOrderService;

    @Override
    public IPage<DetailInformation> pageVo(DetailInformationVo detailInformationVo) {
        Integer pageSize = detailInformationVo.getPageSize();
        Integer offset = (detailInformationVo.getCurrent() - 1) * pageSize;
        System.out.println(offset);
        Page<DetailInformation> page = new Page<>();
        page.setCurrent(offset);
        page.setSize(pageSize);

        List<WorkOrder> orderList = workOrderService.list(Wrappers.lambdaQuery(WorkOrder.class)
                .eq(WorkOrder::getType, 1)
                .and(wrapper -> wrapper.isNull(WorkOrder::getReviewTime)
                            .or()
                            .isNotNull(WorkOrder::getFailTime))
        );
        List<Long> notCompleteRecordList = orderList.stream().map(order -> order.getDetailId()).collect(Collectors.toList());

        IPage<DetailInformation> iPage = detailMapper.queryPage(page, detailInformationVo, notCompleteRecordList);
        Long total = Long.valueOf(iPage.getRecords().size());

        iPage.setTotal(total);

        List<DetailInformation> records = iPage.getRecords();
        for (DetailInformation record : records) {
            ImageInformation image = record.getImage();
            String bucketName = image.getBucketName();
            String storageName = image.getStorageName();
            String previewUrl = minIOUService.getPreviewUrl(storageName, bucketName);
            record.setPreviewUrl(previewUrl);
        }

        return iPage;
    }

    @Override
    @Transactional
    public void saveRecord(DetailInformationVo detailInformationVo) {
        String username = detailInformationVo.getUsername();
        String workLeaderName = detailInformationVo.getWorkLeaderName();
        UserInformation user = userService.getOne(new LambdaQueryWrapper<UserInformation>().eq(UserInformation::getUsername, username));
        UserInformation workLeader = userService.getOne(new LambdaQueryWrapper<UserInformation>().eq(UserInformation::getUsername, workLeaderName));
        if (Objects.isNull(user) || Objects.isNull(workLeader)) {
            throw new CustomException(CommonConstant.NO_EXIST_USER);
        }

        DeviceInformation device = detailInformationVo.getDevice();
        Long deviceId = UUIDUtil.generateUUID();
        device.setDeviceId(deviceId);
        deviceService.save(device);

        Long detailId = UUIDUtil.generateUUID();
        detailInformationVo.setDetailId(detailId);
        detailInformationVo.setDevice(device);
        detailMapper.saveRecord(detailInformationVo);
    }

    @Override
    public ChartsVo chartsInfo(List<DetailInformation> records) {
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

        List<Double> relativeHumidityList = records.stream()
                .sorted(Comparator.comparing(DetailInformation::getCreateTime))
                .map(record -> record.getRelativeHumidity())
                .collect(Collectors.toList());

        List<Double> centralHumidityList = records.stream()
                .sorted(Comparator.comparing(DetailInformation::getCreateTime))
                .map(record -> record.getCentralHumidity())
                .collect(Collectors.toList());

        List<Double> maxTempList = records.stream()
                .sorted(Comparator.comparing(DetailInformation::getCreateTime))
                .map(record -> record.getMaxTemp())
                .collect(Collectors.toList());

        List<Double> minTempList = records.stream()
                .sorted(Comparator.comparing(DetailInformation::getCreateTime))
                .map(record -> record.getMinTemp())
                .collect(Collectors.toList());

        List<Double> avgTempList = records.stream()
                .sorted(Comparator.comparing(DetailInformation::getCreateTime))
                .map(record -> record.getAvgTemp())
                .collect(Collectors.toList());

        List<Double> reflectTempList = records.stream()
                .sorted(Comparator.comparing(DetailInformation::getCreateTime))
                .map(record -> record.getReflectedTemp())
                .collect(Collectors.toList());


        SeriesData ambient = SeriesData.builder()
                .name("环境温度(°C)")
                .data(ambientTempList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(0, ambient);

        SeriesData humidity = SeriesData.builder()
                .name("相对湿度(%)")
                .data(relativeHumidityList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(1, humidity);

        SeriesData cHumidity = SeriesData.builder()
                .name("中心湿度(%)")
                .data(centralHumidityList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(2, cHumidity);

        SeriesData maxTemp = SeriesData.builder()
                .name("最高温度(°C)")
                .data(maxTempList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(3, maxTemp);

        SeriesData minTemp = SeriesData.builder()
                .name("最低温度(°C)")
                .data(minTempList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(4, minTemp);

        SeriesData avgTemp = SeriesData.builder()
                .name("平均温度(°C)")
                .data(avgTempList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(5, avgTemp);

        SeriesData reflectedTemp = SeriesData.builder()
                .name("反射温度(°C)")
                .data(reflectTempList)
                .type("line")
                .stack("Total")
                .build();
        seriesDataList.add(6, reflectedTemp);

        chartsVo.setCategories(xList);
        chartsVo.setSeriesDataList(seriesDataList);

        return chartsVo;
    }

    @Override
    public DetailInformation getDetailById(Long detailId) {
        return detailMapper.getDetailById(detailId);
    }

}




