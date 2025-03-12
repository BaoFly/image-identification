package com.bf.image.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 点阵数据表
 * @TableName file_lattice_data
 */
@TableName(value ="file_lattice_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileLatticeData implements Serializable {
    /**
     * 点阵数据主键ID
     */
    @TableId(value = "file_lattice_data_id", type = IdType.ASSIGN_ID)
    private Long fileLatticeDataId;

    /**
     * 红外信息主键ID
     */
    @TableField(value = "detail_id")
    private Long detailId;

    /**
     * 0:修复前 1:修复后
     */
    @TableField(value = "fix_type")
    private Integer fixType;

    /**
     * 文件大小，字节数
     */
    @TableField(value = "file_size")
    private Long fileSize;

    /**
     * 所在桶名称
     */
    @TableField(value = "bucket_name")
    private String bucketName;

    /**
     * 文件原本上传时的名字
     */
    @TableField(value = "file_original_name")
    private String fileOriginalName;

    /**
     * 文件存储在服务器时的名字
     */
    @TableField(value = "file_storage_name")
    private String fileStorageName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 删除标识
     */
    @TableField(value = "delete_flag")
    @TableLogic
    private Integer deleteFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}