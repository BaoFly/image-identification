package com.bf.image.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName deatil_information
 */
@TableName(value ="detail_information")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailInformation implements Serializable {
    /**
     * 表主键ID
     */
    @TableId
    private Long detailId;

    /**
     * 用户
     */
    private String username;

    /**
     * 工作负责人
     */
    private String workLeaderName;

    /**
     * 设备
     */
    private DeviceInformation device;

    /**
     * 图片信息
     */
    private ImageInformation image;

    /**
     * 供电局/区局
     */
    private String powerSupplyStation;

    /**
     * 变电站名称
     */
    private String substationName;

    /**
     * 配电房名称
     */
    private String distributionRoomName;

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
     * 反射温度
     */
    private Double reflectedTemp;

    /**
     * 馈线
     */
    private Double feeder;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 前端传过来的字段，支持MultipartFile类型 和 base64的图片
     */
    @TableField(exist = false)
    private Object imageObj;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}