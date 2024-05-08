package com.bf.image.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 
 * @TableName day_summary_record
 */
@TableName(value ="day_summary_record")
@Data
public class DaySummaryRecord implements Serializable {
    /**
     * 汇总表记录ID
     */
    @TableId(value = "day_summary_record_id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long daySummaryRecordId;

    /**
     * 这天中用户增加
     */
    @TableField(value = "user_count")
    private Integer userCount;

    /**
     * 这天中用户新增
     */
    @TableField(value = "user_add_count")
    private Integer userAddCount;

    /**
     * tev记录条数
     */
    @TableField(value = "tev_count")
    private Long tevCount;

    /**
     * tev新增条数
     */
    @TableField(value = "tev_add_count")
    private Integer tevAddCount;

    /**
     * 红外记录条数
     */
    @TableField(value = "detail_count")
    private Long detailCount;

    /**
     * 红外新增条数
     */
    @TableField(value = "detail_add_count")
    private Integer detailAddCount;

    /**
     * 设备条数
     */
    @TableField(value = "device_count")
    private Integer deviceCount;

    /**
     * 设备新增条数
     */
    @TableField(value = "device_add_count")
    private Integer deviceAddCount;

    /**
     * 钉钉预警群个数
     */
    @TableField(value = "message_warning_count")
    private Integer messageWarningCount;

    /**
     * 钉钉群增加个数
     */
    @TableField(value = "message_warning_add_count")
    private Integer messageWarningAddCount;

    /**
     * 发送预警消息条数
     */
    @TableField(value = "message_warning_record_count")
    private Integer messageWarningRecordCount;

    /**
     * 新增预警条数
     */
    @TableField(value = "message_warning_record_add_count")
    private Integer messageWarningRecordAddCount;

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
     * 逻辑删除标识
     */
    @TableField(value = "is_delete")
    @TableLogic
    private byte[] isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}