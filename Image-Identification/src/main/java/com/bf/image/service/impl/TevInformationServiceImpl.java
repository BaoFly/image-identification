package com.bf.image.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.pojo.DetailInformation;
import com.bf.image.pojo.TevInformation;
import com.bf.image.service.TevInformationService;
import com.bf.image.mapper.TevInformationMapper;
import com.bf.image.vo.ChartsVo;
import com.bf.image.vo.TevVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Gplus-033
* @description 针对表【tev_information】的数据库操作Service实现
* @createDate 2024-04-11 18:19:00
*/
@Service
public class TevInformationServiceImpl extends ServiceImpl<TevInformationMapper, TevInformation>
    implements TevInformationService{

    @Override
    public IPage<DetailInformation> pageVo(TevVo tevVo) {
        return null;
    }

    @Override
    public ChartsVo chartsInfo(List<DetailInformation> records) {
        return null;
    }

    @Override
    public void saveRecord(TevVo tevVo) {

    }
}




