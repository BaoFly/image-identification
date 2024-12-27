package com.bf.image.entity;

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
 * @TableName user_information
 */
@TableName(value ="user_information")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInformation implements Serializable {
    /**
     * 用户表ID
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 用户密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

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
     * 是否已经被删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    /**
     * 性别
     */
    @TableField(value = "sex")
    private Integer sex;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}