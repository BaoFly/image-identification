package com.bf.image.vo;

import com.bf.image.pojo.MessageWarning;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MessageWarningVo extends MessageWarning {

    private Integer current;

    private Integer pageSize;

    private List<Date> dateRange;

}
