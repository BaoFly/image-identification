package com.bf.image.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.pojo.DeviceInformation;
import com.bf.image.pojo.UserInformation;
import com.bf.image.service.DeviceInformationService;
import com.bf.image.mapper.DeviceInformationMapper;
import com.bf.image.vo.DeviceVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
* @author Administrator
* @description 针对表【device_information】的数据库操作Service实现
* @createDate 2023-10-14 18:40:30
*/
@Service
public class DeviceInformationServiceImpl extends ServiceImpl<DeviceInformationMapper, DeviceInformation>
    implements DeviceInformationService{

    @Override
    public Page<DeviceInformation> devicePageVo(DeviceVo deviceVo) {

        Integer pageSize = deviceVo.getPageSize();
        Integer offset = (deviceVo.getCurrent() - 1) * pageSize;
        Page<DeviceInformation> newPage = new Page<>();
        newPage.setCurrent(offset);
        newPage.setSize(pageSize);

        Date startTime = null;
        Date endTime = null;

        if (deviceVo.getDateRange() != null && org.apache.commons.collections.CollectionUtils.isNotEmpty(deviceVo.getDateRange())) {
            startTime = deviceVo.getDateRange().get(0);
            endTime = deviceVo.getDateRange().get(1);
        }

        Page<DeviceInformation> page = this.page(newPage,
                Wrappers.lambdaQuery(DeviceInformation.class)
                        .like(Objects.nonNull(deviceVo.getDeviceName()), DeviceInformation::getDeviceName, deviceVo.getDeviceName())
                        .between(Objects.nonNull(startTime), DeviceInformation::getCreateTime, startTime, endTime)
                        .eq(Objects.nonNull(deviceVo.getDeviceType()), DeviceInformation::getDeviceType, deviceVo.getDeviceType())
                        .orderByDesc(DeviceInformation::getCreateTime)
        );
        return page;
    }
}




