package com.bf.image.vo;

import com.bf.image.pojo.DeviceInformation;
import com.bf.image.pojo.ImageInformation;
import com.bf.image.pojo.WorkOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrderVo extends WorkOrder {

    public String username;

    public String reviewName;

    public List<Date> dateRange;

    public Object bizObj;

    public String previewUrl;

    public ImageInformation image;

    public DeviceInformation device;

    public Integer current;

    public Integer pageSize;
}
