package com.bf.image.mapper;

import com.bf.image.pojo.UserInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【user_information】的数据库操作Mapper
* @createDate 2023-10-14 18:40:50
* @Entity com.bf.image.pojo.UserInformation
*/
@Mapper
public interface UserInformationMapper extends BaseMapper<UserInformation> {

}




