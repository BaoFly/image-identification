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
 * @TableName inspection_work_order
 */
@TableName(value ="inspection_work_order")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InspectionWorkOrder implements Serializable {
    /**
     * 巡检工单ID
     */
    @TableId(value = "inspection_work_order_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long inspectionWorkOrderId;

    /**
     * 派单人
     */
    @TableField(value = "dispatch_name")
    private String dispatchName;

    /**
     * 工单负责人
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 巡检状态,0:未检,1:正常,2:缺陷
     */
    @TableField(value = "inspection_status")
    private Integer inspectionStatus;

    /**
     * 需检设备,0:琶洲新社区#1开关房G01开关柜,1:琶洲新社区#2综合房G02联络柜,2:琶洲新社区#3综合房G03变压器
     */
    @TableField(value = "device_name")
    private Integer deviceName;

    /**
     * 巡检班组,0:海珠班,1:炭步班,2:狮岭班
     */
    @TableField(value = "inspection_team")
    private Integer inspectionTeam;

    /**
     * 红外图像信息ID
     */
    @TableField(value = "detail_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long detailId;

    /**
     * 局放信息ID
     */
    @TableField(value = "tev_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tevId;

    /**
     * 馈线,0:艺苑F14,1:友好F24
     */
    @TableField(value = "feeder")
    private Integer feeder;

    /**
     * 巡检计划开始时间
     */
    @TableField(value = "plan_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planStartTime;

    /**
     * 巡检计划结束时间
     */
    @TableField(value = "plan_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planEndTime;

    /**
     * 数据类型,0:红外数据,1:局放数据
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField(value = "memo")
    private String memo;

    /**
     * 删除标志
     */
    @TableField(value = "delete_flag")
    @TableLogic
    private Integer deleteFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}