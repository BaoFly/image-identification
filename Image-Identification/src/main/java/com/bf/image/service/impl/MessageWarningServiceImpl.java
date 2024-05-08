package com.bf.image.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.exception.CustomException;
import com.bf.image.pojo.DeviceInformation;
import com.bf.image.pojo.MessageWarning;
import com.bf.image.service.MessageWarningService;
import com.bf.image.mapper.MessageWarningMapper;
import com.bf.image.vo.MessageWarningVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
* @author Gplus-033
* @description 针对表【message_warning】的数据库操作Service实现
* @createDate 2024-05-06 12:28:19
*/
@Service
public class MessageWarningServiceImpl extends ServiceImpl<MessageWarningMapper, MessageWarning>
    implements MessageWarningService{

    @Override
    public Page<MessageWarning> pageVo(MessageWarningVo messageWarningVo) {

        Integer pageSize = messageWarningVo.getPageSize();
        Integer offset = (messageWarningVo.getCurrent() - 1) * pageSize;
        Page<MessageWarning> newPage = new Page<>();
        newPage.setCurrent(offset);
        newPage.setSize(pageSize);

        Date startTime = null;
        Date endTime = null;

        if (messageWarningVo.getDateRange() != null && org.apache.commons.collections.CollectionUtils.isNotEmpty(messageWarningVo.getDateRange())) {
            startTime = messageWarningVo.getDateRange().get(0);
            endTime = messageWarningVo.getDateRange().get(1);
        }

        Page<MessageWarning> page = this.page(newPage,
                Wrappers.lambdaQuery(MessageWarning.class)
                        .like(Objects.nonNull(messageWarningVo.getMessageName()), MessageWarning::getMessageName, messageWarningVo.getMessageName())
                        .between(Objects.nonNull(startTime), MessageWarning::getCreateTime, startTime, endTime)
                        .like(Objects.nonNull(messageWarningVo.getMessageMemo()), MessageWarning::getMessageMemo, messageWarningVo.getMessageMemo())
                        .orderByDesc(MessageWarning::getCreateTime)
        );

        return page;

    }

    @Override
    public void add(MessageWarning messageWarning) {
        String messageUrl = messageWarning.getMessageUrl();
        if (StringUtils.isBlank(messageUrl)) {
            throw new CustomException("URL不能为空");
        }
        MessageWarning one = this.getOne(Wrappers.lambdaQuery(MessageWarning.class).eq(MessageWarning::getMessageUrl, messageUrl));
        if (Objects.nonNull(one)) {
            throw new CustomException("不能创建相同URL的钉钉群");
        }
        this.save(messageWarning);
    }
}




