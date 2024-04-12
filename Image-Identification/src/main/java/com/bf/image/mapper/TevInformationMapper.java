package com.bf.image.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.pojo.TevInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bf.image.vo.DetailInformationVo;
import com.bf.image.vo.TevVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/**
* @author Gplus-033
* @description 针对表【tev_information】的数据库操作Mapper
* @createDate 2024-04-11 18:19:00
* @Entity com.bf.image.pojo.TevInformation
*/
public interface TevInformationMapper extends BaseMapper<TevInformation> {
}




