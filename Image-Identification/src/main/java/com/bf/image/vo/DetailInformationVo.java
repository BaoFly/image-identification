package com.bf.image.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.bf.image.pojo.DetailInformation;
import com.bf.image.pojo.DeviceInformation;
import com.bf.image.pojo.ImageInformation;
import com.bf.image.pojo.UserInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailInformationVo {


    private Long detailId;

    /**
     * 供电局/区局
     */
    private String powerSupplyStation;

    /**
     * 变电站名称
     */
    private String substationName;

    /**
     * 巡检班组
     */
    private String inspectionTeam;

    /**
     * 环境温度
     */
    private Double ambientTemp;

    /**
     * 相对湿度
     */
    private Double relativeHumidity;

    /**
     * 中心湿度
     */
    private Double centralHumidity;

    /**
     * 最大温度
     */
    private Double maxTemp;

    /**
     * 最小温度
     */
    private Double minTemp;

    /**
     * 平均温度
     */
    private Double avgTemp;

    /**
     * 馈线
     */
    private Double feeder;

    /**
     * 反射温度
     */
    private Double emissionTemp;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 前端传过来的字段，支持MultipartFile类型 和 base64的图片
     */
    private Object imageObj;

    private static final long serialVersionUID = 1L;

    /**
     * 设备表ID
     */
    private Long deviceId;

    /**
     * 0：开关柜 1：变压器 2：电缆 3：其他
     */
    private Integer deviceType;

    /**
     * 设备名称
     */
    private String deviceName;

    private Long userId;

    /**
     * 用户名
     */
    private String username;

    private Long workLeaderId;

    /**
     * 图片ID
     */
    private Long imageId;

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
     * 创建人名称
     */
    private String creatorName;

}
