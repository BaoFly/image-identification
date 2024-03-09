package com.bf.image.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileVo implements Serializable {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 预览链接
     */
    private String previewUrl;

    /**
     * 所属模块（转换为"桶"）
     */
    private String module;

    /**
     * 图片ID
     */
    private Long imageId;

    /**
     * 图片大小
     */
    private Long size;

    /**
     * 原生名字
     */
    private String originalName;
}
