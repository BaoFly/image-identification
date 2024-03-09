package com.bf.image.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.config.MinIOConfig;
import com.bf.image.pojo.ImageInformation;
import com.bf.image.service.ImageInformationService;
import com.bf.image.mapper.ImageInformationMapper;
import com.bf.image.service.MinIOUService;
import com.bf.image.vo.FileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author Administrator
* @description 针对表【image_information】的数据库操作Service实现
* @createDate 2023-10-14 18:40:41
*/
@Service
public class ImageInformationServiceImpl extends ServiceImpl<ImageInformationMapper, ImageInformation>
    implements ImageInformationService{

    @Autowired
    private MinIOConfig minIOConfig;

    @Autowired
    private MinIOUService minIOUService;

    @Override
    public void saveImageByFileVo(List<FileVo> list) {
        String bucketName = minIOConfig.getBucketName();
        List<ImageInformation> imageInformationList = new ArrayList<>();
        for (FileVo fileVo : list) {
            ImageInformation imageInformation = ImageInformation.builder()
                    .imageId(fileVo.getImageId())
                    .bucketName(bucketName)
                    .imageSize(fileVo.getSize())
                    .storageName(fileVo.getFileName())
                    .imageName(fileVo.getOriginalName())
                    .createTime(new Date())
                    .updateTime(new Date())
                    .build();
            imageInformationList.add(imageInformation);
        }
        this.saveBatch(imageInformationList);
    }

    @Override
    public void removeRecord(FileVo fileVo) {
        this.removeById(fileVo.getImageId());
        minIOUService.removeFile(fileVo.getModule(), fileVo.getFileName());
    }
}




