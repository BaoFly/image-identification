package com.bf.image.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadBody {
    /**
     * 多文件
     */
    private MultipartFile[] files;

    /**
     * 所属模块（转换为"桶"）
     */
    private String module;

    /**
     * 自定义文件名
     */
    private String fileName;
}
