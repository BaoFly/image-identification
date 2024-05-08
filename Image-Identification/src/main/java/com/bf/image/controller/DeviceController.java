package com.bf.image.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.pojo.DeviceInformation;
import com.bf.image.pojo.UserInformation;
import com.bf.image.service.DeviceInformationService;
import com.bf.image.vo.DeviceVo;
import com.bf.image.vo.ResultJson;
import com.bf.image.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Device相关接口")
@RestController
@CrossOrigin
public class DeviceController {

    @Autowired
    private DeviceInformationService deviceService;

    @ApiOperation("device分页列表接口")
    @PostMapping("device/pageVo")
    public ResultJson devicePageVo(@RequestBody DeviceVo deviceVo) {
        Page<DeviceInformation> page = deviceService.devicePageVo(deviceVo);
        return ResultJson.success(page);
    }

}
