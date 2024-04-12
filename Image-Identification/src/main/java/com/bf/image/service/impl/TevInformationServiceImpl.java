package com.bf.image.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.config.MinIOConfig;
import com.bf.image.pojo.*;
import com.bf.image.service.*;
import com.bf.image.mapper.TevInformationMapper;
import com.bf.image.vo.ChartsVo;
import com.bf.image.vo.TevVo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.bf.image.utils.JsonParser.jsonParser;

/**
* @author Gplus-033
* @description 针对表【tev_information】的数据库操作Service实现
* @createDate 2024-04-11 18:19:00
*/
@Service
public class TevInformationServiceImpl extends ServiceImpl<TevInformationMapper, TevInformation>
    implements TevInformationService{

    @Autowired
    private TevInformationMapper tevMapper;

    @Autowired
    private UserInformationService userService;

    @Autowired
    private DeviceInformationService deviceService;

    @Autowired
    private MinIOUService minIOUService;

    @Autowired
    private ImageInformationService imageService;

    @Autowired
    private MinIOConfig minIOConfig;


    @Override
    public IPage<TevVo> pageVo(TevVo tevVo) {
        Integer pageSize = tevVo.getPageSize();
        Integer offset = (tevVo.getCurrent() - 1) * pageSize;
        Page<TevInformation> newPage = new Page<>();
        newPage.setCurrent(offset);
        newPage.setSize(pageSize);

        List<UserInformation> userList = userService.list(Wrappers.lambdaQuery(UserInformation.class)
                .like(StringUtils.isNotBlank(tevVo.getUser().getUsername()), UserInformation::getUsername, tevVo.getUser().getUsername())
                .like(StringUtils.isNotBlank(tevVo.getWorkLeader().getUsername()), UserInformation::getUsername, tevVo.getWorkLeader().getUsername()));
        List<Long> userIdList = userList.stream().map(user -> user.getUserId()).collect(Collectors.toList());
        Map<Long, String> userIdNameMap = userList.stream().collect(Collectors.toMap(
                user -> user.getUserId(),
                user -> user.getUsername()
        ));

        List<DeviceInformation> deviceList = deviceService.list(Wrappers.lambdaQuery(DeviceInformation.class)
                .eq(Objects.nonNull(tevVo.getDevice().getDeviceId()), DeviceInformation::getDeviceId, tevVo.getDevice().getDeviceId())
                .like(Objects.nonNull(tevVo.getDevice().getDeviceName()), DeviceInformation::getDeviceName, tevVo.getDevice().getDeviceName())
                .eq(Objects.nonNull(tevVo.getDevice().getDeviceType()), DeviceInformation::getDeviceType, tevVo.getDevice().getDeviceType())
        );
        List<Long> deviceIdList = deviceList.stream().map(device -> device.getDeviceId()).collect(Collectors.toList());
        Map<Long, DeviceInformation> deviceIdMap = deviceList.stream().collect(Collectors.toMap(
                device -> device.getDeviceId(),
                device -> device
        ));

        Date startTime = null;
        Date endTime = null;
        Date occStartTime = null;
        Date occEndTime = null;

        if (tevVo.getDateRange() != null && CollectionUtils.isNotEmpty(tevVo.getDateRange())) {
            startTime = tevVo.getDateRange().get(0);
            endTime = tevVo.getDateRange().get(1);
        }

        if (tevVo.getOccurrenceTimeRange() != null && CollectionUtils.isNotEmpty(tevVo.getOccurrenceTimeRange())) {
            occStartTime = tevVo.getOccurrenceTimeRange().get(0);
            occEndTime = tevVo.getOccurrenceTimeRange().get(1);
        }

        Page<TevInformation> page = this.page(newPage,
                Wrappers.lambdaQuery(TevInformation.class)
                        .in(CollectionUtils.isNotEmpty(userIdList), TevInformation::getUserId, userIdList)
                        .in(CollectionUtils.isNotEmpty(userIdList), TevInformation::getLeaderId, userIdList)
                        .in(CollectionUtils.isNotEmpty(deviceIdList), TevInformation::getDeviceId, deviceIdList)
                        .between(Objects.nonNull(startTime), TevInformation::getCreateTime, startTime, endTime)
                        .between(Objects.nonNull(occStartTime), TevInformation::getOccurrenceTime, occStartTime, occEndTime)
                        .eq(Objects.nonNull(tevVo.getQuasiPeakValue()), TevInformation::getQuasiPeakValue, tevVo.getQuasiPeakValue())
                        .eq(Objects.nonNull(tevVo.getMaxValue()), TevInformation::getMaxValue, tevVo.getMaxValue())
                        .eq(Objects.nonNull(tevVo.getMinValue()), TevInformation::getMinValue, tevVo.getMinValue())
                        .eq(Objects.nonNull(tevVo.getPolarity()), TevInformation::getPolarity, tevVo.getPolarity())
                        .eq(Objects.nonNull(tevVo.getRepetitionRate()), TevInformation::getRepetitionRate, tevVo.getRepetitionRate())
        );

        CollectionType listType = jsonParser.getTypeFactory().constructCollectionType(ArrayList.class, TevVo.class);
        List<TevVo> list = jsonParser.convertValue(page.getRecords(), listType);
        Page<TevVo> objectPage = new Page<TevVo>(page.getCurrent(), page.getSize(), page.getTotal());

        if (CollectionUtils.isEmpty(list)) {
            return objectPage;
        }

        List<Long> imageIdList = list.stream().map(tev -> tev.getImageId()).collect(Collectors.toList());
        List<ImageInformation> imageList = imageService.list(Wrappers.lambdaQuery(ImageInformation.class)
                .in(CollectionUtils.isNotEmpty(imageIdList), ImageInformation::getImageId, imageIdList));
        Map<Long, ImageInformation> imageIdMap = imageList.stream().collect(Collectors.toMap(
                image -> image.getImageId(),
                image -> image
        ));

        for (TevVo vo : list) {
            String username = userIdNameMap.get(vo.getUserId());
            String leaderName = userIdNameMap.get(vo.getLeaderId());
            Long imageId = vo.getImageId();
            ImageInformation image = imageIdMap.get(imageId);
            Long deviceId = vo.getDeviceId();
            DeviceInformation device = deviceIdMap.get(deviceId);

            vo.setImage(image);
            vo.setUser(UserInformation.builder().username(username).build());
            vo.setWorkLeader(UserInformation.builder().username(leaderName).build());
            vo.setDevice(device);
            vo.setPreviewUrl(minIOUService.getPreviewUrl(image.getStorageName(), minIOConfig.getBucketName()));
        }
        objectPage.setRecords(list);

        return objectPage;
    }

    @Override
    public ChartsVo chartsInfo(List<TevVo> records) {
        return null;
    }

    @Override
    public void saveRecord(TevVo tevVo) {

    }
}




