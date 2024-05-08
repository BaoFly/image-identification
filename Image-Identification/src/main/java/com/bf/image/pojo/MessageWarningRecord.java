package com.bf.image.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @TableName message_warning_record
 */
@TableName(value ="message_warning_record")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageWarningRecord implements Serializable {
    /**
     * 钉钉预警记录ID
     */
    @TableId(value = "message_warning_record_id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long messageWarningRecordId;

    /**
     * 钉钉预警ID
     */
    @TableField(value = "message_warning_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long messageWarningId;

    /**
     * 预警的业务ID
     */
    @TableField(value = "message_biz_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long messageBizId;

    /**
     * 1:红外图像 2：局部TEV
     */
    @TableField(value = "message_type")
    private Integer messageType;

    /**
     * 发送内容
     */
    @TableField(value = "message_content")
    private String messageContent;

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
    private byte[] isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}