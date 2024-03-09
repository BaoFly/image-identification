package com.bf.image.service;

import com.bf.image.pojo.ImageInformation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bf.image.vo.FileVo;

import java.util.List;

/**
* @author Administrator
* @description 针对表【image_information】的数据库操作Service
* @createDate 2023-10-14 18:40:41
*/
public interface ImageInformationService extends IService<ImageInformation> {


    void saveImageByFileVo(List<FileVo> list);

    void removeRecord(FileVo fileVo);
}
