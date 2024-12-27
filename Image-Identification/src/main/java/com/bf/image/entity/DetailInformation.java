package com.bf.image.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName detail_information
 */
@TableName(value ="detail_information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailInformation implements Serializable {
    /**
     * 表主键ID
     */
    @TableId(value = "detail_id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long detailId;

    /**
     * 图片信息ID
     */
    @TableField(value = "image_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long imageId;

    /**
     * 供电局/区局,0:天河供电局,1:白云供电局,2:越秀供电局,3:荔湾供电局,4:海珠供电局,5:黄埔供电局,6:花都供电局,7:番禺供电局,8:增城供电局,9:从化供电局,10:南沙供电局
     */
    @TableField(value = "power_supply_station")
    private Integer powerSupplyStation;

    /**
     * 配电房名称,0:琶洲新社区#1开关房,1:琶洲新社区#2综合房,2:琶洲新社区#3综合房
     */
    @TableField(value = "distribution_room_name")
    private Integer distributionRoomName;

    /**
     * 设备类型,0:开关柜,1:联络柜,2:变压器
     */
    @TableField(value = "device_type")
    private Integer deviceType;

    /**
     * 环境温度
     */
    @TableField(value = "ambient_temp")
    private Double ambientTemp;

    /**
     * 中心温度
     */
    @TableField(value = "central_temp")
    private Double centralTemp;

    /**
     * 最大温度
     */
    @TableField(value = "max_temp")
    private Double maxTemp;

    /**
     * 最小温度
     */
    @TableField(value = "min_temp")
    private Double minTemp;

    /**
     * 巡检详情
     */
    @TableField(value = "inspection_detail")
    private String inspectionDetail;

    /**
     * 备注
     */
    @TableField(value = "memo")
    private String memo;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag")
    private Integer deleteFlag;

    @TableField(exist = false)
    @TableLogic
    private static final long serialVersionUID = 1L;
}