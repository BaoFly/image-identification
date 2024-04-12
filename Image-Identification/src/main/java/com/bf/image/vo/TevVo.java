package com.bf.image.vo;

import com.bf.image.pojo.DeviceInformation;
import com.bf.image.pojo.ImageInformation;
import com.bf.image.pojo.TevInformation;
import com.bf.image.pojo.UserInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TevVo extends TevInformation {

    public UserInformation user;

    public UserInformation workLeader;

    public ImageInformation image;

    public DeviceInformation device;

}
