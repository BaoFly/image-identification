package com.bf.image.vo;

import com.bf.image.pojo.DetailInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailInformationVo extends DetailInformation {

    private Long deviceId;
    private Long imageId;
    private Long userId;
    private Long workLeaderId;
    /**
     * 0：开关柜 1：变压器 2：电缆 3：其他
     */
    private Integer deviceType;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 图片大小，单位为字节数
     */
    private Long imageSize;

    /**
     * 图片存储路径
     */
    private String imagePath;

    /**
     * 图片原生名字
     */
    private String imageName;

    /**
     * 图片存储在服务器的名字
     */
    private String storageName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

}
