package com.bf.image.vo;

import com.bf.image.pojo.DeviceInformation;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DeviceVo extends DeviceInformation {

    private Integer current;

    private Integer pageSize;

    private List<Date> dateRange;

}
