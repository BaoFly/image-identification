package com.bf.image.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.pojo.MessageWarning;
import com.bf.image.service.MessageWarningService;
import com.bf.image.vo.DeviceVo;
import com.bf.image.vo.MessageWarningVo;
import com.bf.image.vo.ResultJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "MessageWarning相关接口")
@RestController
@CrossOrigin
public class MessageWarningController {

    @Autowired
    private MessageWarningService messageWarningService;

    @ApiOperation("messageWarning分页列表接口")
    @PostMapping("messageWarning/pageVo")
    public ResultJson pageVo(@RequestBody MessageWarningVo messageWarningVo) {
        Page<MessageWarning> page = messageWarningService.pageVo(messageWarningVo);
        return ResultJson.success(page);
    }

    @ApiOperation("messageWarning分页列表接口")
    @PostMapping("messageWarning/add")
    public ResultJson add(@RequestBody MessageWarning messageWarning) {
        messageWarningService.add(messageWarning);
        return ResultJson.success();
    }



}
