package com.bf.image.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.pojo.MessageWarningRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bf.image.vo.MessageWarningRecordVo;
import org.springframework.stereotype.Service;

/**
* @author Gplus-033
* @description 针对表【message_warning_record】的数据库操作Service
* @createDate 2024-05-06 12:28:27
*/
public interface MessageWarningRecordService extends IService<MessageWarningRecord> {

    Page<MessageWarningRecordVo> pageVo(MessageWarningRecordVo messageWarningRecordVo);

    void sendMsg(MessageWarningRecordVo messageWarningRecordVo);
}
