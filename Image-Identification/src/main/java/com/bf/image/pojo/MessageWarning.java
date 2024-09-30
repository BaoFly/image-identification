package com.bf.image.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName message_warning
 */
@TableName(value ="message_warning")
@Data
@Accessors(chain = true)
public class MessageWarning implements Serializable {
    /**
     * 钉钉群预警ID
     */
    @TableId(value = "message_warning_id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long messageWarningId;

    /**
     * 钉钉群URL
     */
    @TableField(value = "message_url")
    private String messageUrl;

    /**
     * 钉钉群名称
     */
    @TableField(value = "message_name")
    private String messageName;

    /**
     * 群备注
     */
    @TableField(value = "message_memo")
    private String messageMemo;

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