package com.bf.image.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.pojo.DeviceInformation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bf.image.vo.DeviceVo;

/**
* @author Administrator
* @description 针对表【device_information】的数据库操作Service
* @createDate 2023-10-14 18:40:30
*/
public interface DeviceInformationService extends IService<DeviceInformation> {

    Page<DeviceInformation> devicePageVo(DeviceVo deviceVo);
}
