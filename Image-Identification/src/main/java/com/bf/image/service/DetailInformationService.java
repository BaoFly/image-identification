package com.bf.image.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.pojo.DetailInformation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bf.image.vo.DetailInformationVo;

import javax.servlet.ServletContext;
import java.util.List;

/**
* @author Administrator
* @description 针对表【deatil_information】的数据库操作Service
* @createDate 2023-10-14 18:40:13
*/
public interface DetailInformationService extends IService<DetailInformation> {

    void uploadInfo(DetailInformation detailInformation, String fullUrl);

    DetailInformation getNewestInfo(DetailInformation detailInformation);

    List<DetailInformation> getDetailInfoByCondition(DetailInformationVo detailInformationVo);

    DetailInformation convert(DetailInformationVo detailInformationVo);
}
