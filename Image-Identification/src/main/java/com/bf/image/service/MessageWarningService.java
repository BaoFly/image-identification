package com.bf.image.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.pojo.MessageWarning;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bf.image.vo.MessageWarningVo;
import org.springframework.stereotype.Service;

/**
* @author Gplus-033
* @description 针对表【message_warning】的数据库操作Service
* @createDate 2024-05-06 12:28:19
*/
public interface MessageWarningService extends IService<MessageWarning> {

    Page<MessageWarning> pageVo(MessageWarningVo messageWarningVo);

    void add(MessageWarning messageWarning);
}
