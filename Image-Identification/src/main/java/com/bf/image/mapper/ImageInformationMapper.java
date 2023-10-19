package com.bf.image.mapper;

import com.bf.image.pojo.ImageInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【image_information】的数据库操作Mapper
* @createDate 2023-10-14 18:40:41
* @Entity com.bf.image.pojo.ImageInformation
*/
@Mapper
public interface ImageInformationMapper extends BaseMapper<ImageInformation> {

}




