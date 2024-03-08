package com.bf.image.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName image_information
 */
@TableName(value ="image_information")
@Data
public class ImageInformation implements Serializable {
    /**
     * 图片ID
     */
    @TableId
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
     * 图片原本名字
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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}