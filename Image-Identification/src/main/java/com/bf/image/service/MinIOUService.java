package com.bf.image.service;

import com.bf.image.pojo.FileUploadBody;
import com.bf.image.vo.FileVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface MinIOUService {

    Boolean removeFile(String bucketName, String fileName);

    void existBucket(String name);

    List<FileVo> uploadFile(FileUploadBody body);

    String getPreviewUrl(String fileName, String bucketName);

    void downloadFile(HttpServletResponse response, String fileName, String bucketName);
}
