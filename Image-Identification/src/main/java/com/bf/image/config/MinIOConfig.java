package com.bf.image.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class MinIOConfig {
    /**
     * 服务地址
     */
    private String url;

    /**
     * 用户名
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 预览到期时间（小时）
     */
    private Integer previewExpiry;

    /**
     * 公网IP
     */
    private String publicAddr;

    /**
     * 默认图片名称
     */
    private String defaultPictureName;


    @Bean
    public MinioClient getMinIOClient()
    {
        return MinioClient.builder().endpoint(url).credentials(accessKey, secretKey).build();
    }
}


