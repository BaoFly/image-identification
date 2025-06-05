package com.bf.image.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.bf.image.config.MinIOConfig;
import com.bf.image.constant.CommonConstant;
import com.bf.image.exception.CustomException;
import com.bf.image.entity.FileUploadBody;
import com.bf.image.utils.UUIDUtil;
import com.bf.image.domin.vo.FileVo;
import io.minio.*;
import io.minio.http.Method;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class MinIOUServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(MinIOUServiceImpl.class);

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinIOConfig minIOConfig;


    /**
     * 删除文件
     *
     * @param bucketName minio bucket 名称
     * @param fileName 文件名
     * @return
     */
    public Boolean removeFile(String bucketName, String fileName) {
        try {
            //判断桶是否存在
            boolean res = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (res) {
                //删除文件
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName)
                        .object(fileName).build());
            }
        } catch (Exception e) {
            log.error("minio 删除文件失败");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * description: 判断bucket是否存在，不存在则创建
     * @return: void
     */
    public void existBucket(String name) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<FileVo> uploadFile(FileUploadBody body){
        MultipartFile[] files = body.getFiles();
        if (Objects.nonNull(files) && files.length > 0) {
            String bucketName = minIOConfig.getBucketName();
            List<FileVo> resultList = new ArrayList<>();
            for (MultipartFile file : files) {
                try {
                    log.info("file: " + file);
                    Long size = Long.valueOf(file.getBytes().length);
//                    if (size >= 2* 1024 * 1024) {
//                        throw new CustomException(CommonConstant.UPLOAD_OVER_SIZE);
//                    }
                    if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                    }
                    String fileName = file.getOriginalFilename();
                    if (StringUtils.isNotBlank(body.getFileName())){
                        fileName = body.getFileName();
                    }
                    StringBuffer fileNameSB = new StringBuffer();
                    fileNameSB.append(System.currentTimeMillis()).append("_").append(bucketName).append("_");
                    if (StringUtils.isNotBlank(fileName)){
                        fileNameSB.append(fileName.replaceAll(",", ""));
                    }
                    fileName = fileNameSB.toString();
                    PutObjectArgs args = PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build();
                    minioClient.putObject(args);
                    FileVo fileVo=new FileVo();
                    fileVo.setFileName(fileName);
                    fileVo.setSize(size);
                    fileVo.setModule(bucketName);
                    fileVo.setOriginalName(file.getOriginalFilename());
                    fileVo.setImageId(String.valueOf(UUIDUtil.generateUUID()));
                    fileVo.setPreviewUrl(getPreviewUrl(fileName, bucketName));
                    resultList.add(fileVo);
                } catch (Exception e) {
                    log.error("上传图片失败，File：【{}】", JSONObject.toJSONString(file));
                    throw new CustomException(e.getMessage());
                }
            }
            return resultList;
        }
        return Collections.emptyList();
    }

    public String getPreviewUrl(String storageName, String bucketName) {
//        if (StringUtils.isNotBlank(storageName)) {
//            bucketName = StringUtils.isNotBlank(bucketName) ? bucketName : minIOConfig.getBucketName();
//            String previewURL = "";
//            try {
//                minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(storageName).build());
//                if (null != minIOConfig.getPreviewExpiry()){
//                    previewURL = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(storageName).expiry(minIOConfig.getPreviewExpiry(), TimeUnit.HOURS).build());
//                }else {
//                    previewURL = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(storageName).build());
//                }
//            } catch (Exception e) {
//                log.error("在文件存储中无FileName：【{}】", storageName);
//                previewURL = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(minIOConfig.getDefaultPictureName()).build());
//            } finally {
//                try {
//                    // 原始预览URL（包含签名参数，如 http://minio:9000/my-image-bucket/file.jpg?X-Amz-Signature=...）
//                    URI originalUri = new URI(previewURL);
//
//                    // 解析外网穿透地址（publicAddr）
//                    URI publicUri = new URI(minIOConfig.getPublicAddr());
//                    String protocol = publicUri.getScheme();
//                    String host = publicUri.getHost();
//                    int port = publicUri.getPort();
//
//                    // 关键修改：保留原始 URL 的查询参数（包括签名信息）
//                    URI modifiedUri = new URI(
//                            protocol,                // 使用外网协议（http/https）
//                            null,                    // 无用户信息
//                            host,                    // 外网域名（如 aifuturedxe.cn）
//                            port,                    // 外网端口（如 17433）
//                            originalUri.getPath(),   // 保留路径（如 /my-image-bucket/file.jpg）
//                            originalUri.getQuery(),  // 保留原始查询参数（如 X-Amz-Signature=...）
//                            originalUri.getFragment()
//                    );
//
//                    String modifiedPreviewURL = modifiedUri.toString();
//                    log.info("外网预览地址（含签名参数）: {}", modifiedPreviewURL);
//                    return modifiedPreviewURL;
//                } catch (URISyntaxException e) {
//                    throw new RuntimeException("URL 生成失败: " + e.getMessage());
//                }
//            }
//        } else {
//            return null;
//        }
        String publicAddr = minIOConfig.getPublicAddr();
        bucketName = StringUtils.isNotBlank(bucketName) ? bucketName : minIOConfig.getBucketName();
        return publicAddr + "/" + bucketName + "/" + storageName;
    }

    public void downloadFile(HttpServletResponse response, String fileName, String bucketName) {
        if (StringUtils.isNotBlank(fileName)) {
            bucketName = StringUtils.isNotBlank(bucketName) ? bucketName : minIOConfig.getBucketName();
            try {
                ObjectStat objectStat = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build());
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setContentType(objectStat.contentType());
                response.setCharacterEncoding("UTF-8");
                InputStream inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
                IOUtils.copy(inputStream, response.getOutputStream());
            } catch (Exception e) {
                throw new RuntimeException("无该图片信息，该图片为默认图片，请勿下载");
            }
        }
    }


}
