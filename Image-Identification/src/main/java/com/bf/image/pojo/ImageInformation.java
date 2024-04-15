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
 * @TableName image_information
 */
@TableName(value ="image_information")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageInformation implements Serializable {
    /**
     * 图片ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long imageId;

    /**
     * 图片大小，单位为字节数
     */
    private Long imageSize;

    /**
     * 所在桶名称
     */
    private String bucketName;

    /**
     * 图片原本名字
     */
    private String imageName;

    /**
     * 图片存储在服务器的名字
     */
    private String storageName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    /**
     * 创建时间
     */
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}