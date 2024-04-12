package com.bf.image.vo;

import com.bf.image.pojo.DeviceInformation;
import com.bf.image.pojo.ImageInformation;
import com.bf.image.pojo.TevInformation;
import com.bf.image.pojo.UserInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TevVo extends TevInformation {

    public UserInformation user;

    public UserInformation workLeader;

    public ImageInformation image;

    public DeviceInformation device;

    public List<Date> occurrenceTimeRange;

    public List<Date> dateRange;

    public Integer current;

    public Integer pageSize;

    private String previewUrl;

}
