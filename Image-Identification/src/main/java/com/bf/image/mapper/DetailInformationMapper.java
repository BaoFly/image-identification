package com.bf.image.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.pojo.DetailInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bf.image.vo.DetailInformationVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【deatil_information】的数据库操作Mapper
* @createDate 2023-10-14 18:40:13
* @Entity com.bf.image.pojo.DetailInformation
*/
@Mapper
public interface DetailInformationMapper extends BaseMapper<DetailInformation> {

    void insertBySelf(DetailInformation detailInformation);

    Long selectCount(@Param("detailInformationVo") DetailInformationVo detailInformationVo);

    IPage<DetailInformation> queryPage(@Param("page") Page<DetailInformation> page,
                                      @Param("detailInformationVo") DetailInformationVo detailInformationVo);

}




