package com.bf.image.vo;

import com.bf.image.pojo.MessageWarningRecord;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MessageWarningRecordVo extends MessageWarningRecord {

    private String messageMemo;

    private String messageUrl;

    private String messageName;

    private Integer current;

    private Integer pageSize;

    private List<Date> dateRange;


}
