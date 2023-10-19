package com.bf.image.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.pojo.ImageInformation;
import com.bf.image.service.ImageInformationService;
import com.bf.image.mapper.ImageInformationMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【image_information】的数据库操作Service实现
* @createDate 2023-10-14 18:40:41
*/
@Service
public class ImageInformationServiceImpl extends ServiceImpl<ImageInformationMapper, ImageInformation>
    implements ImageInformationService{

}




