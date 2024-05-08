package com.bf.image.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.pojo.MessageWarningRecord;
import com.bf.image.service.MessageWarningRecordService;
import com.bf.image.vo.MessageWarningRecordVo;
import com.bf.image.vo.ResultJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "发送记录")
@RestController
@CrossOrigin
public class MessageWarningRecordController {

    @Autowired
    private MessageWarningRecordService messageWarningRecordService;

    @ApiOperation("MessageWarningRecord分页列表接口")
    @PostMapping("messageWarningRecord/pageVo")
    public ResultJson pageVo(@RequestBody MessageWarningRecordVo messageWarningRecordVo) {
        Page<MessageWarningRecordVo> page = messageWarningRecordService.pageVo(messageWarningRecordVo);
        return ResultJson.success(page);
    }

    @ApiOperation("MessageWarningRecord生成记录接口")
    @PostMapping("messageWarningRecord/send")
    public ResultJson sendMsg(@RequestBody MessageWarningRecordVo messageWarningRecordVo) {
        messageWarningRecordService.sendMsg(messageWarningRecordVo);
        return ResultJson.success();
    }

}
