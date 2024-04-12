package com.bf.image.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bf.image.pojo.DetailInformation;
import com.bf.image.pojo.TevInformation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bf.image.vo.ChartsVo;
import com.bf.image.vo.TevVo;

import java.util.List;

/**
* @author Gplus-033
* @description 针对表【tev_information】的数据库操作Service
* @createDate 2024-04-11 18:19:00
*/
public interface TevInformationService extends IService<TevInformation> {

    IPage<TevVo> pageVo(TevVo tevVo);

    ChartsVo chartsInfo(List<TevVo> records);

    void saveRecord(TevVo tevVo);
}
