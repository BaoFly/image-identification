package com.bf.image.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bf.image.domin.vo.ChartsVo;
import com.bf.image.domin.vo.TevVo;
import com.bf.image.service.impl.ImageInformationServiceImpl;
import com.bf.image.service.impl.MinIOUServiceImpl;
import com.bf.image.service.impl.TevInformationServiceImpl;
import com.bf.image.domin.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "局部Tev相关接口")
@RestController
@CrossOrigin
public class TevController {

    @Autowired
    private TevInformationServiceImpl tevService;

    @Autowired
    private MinIOUServiceImpl minIOUService;

    @Autowired
    private ImageInformationServiceImpl imageService;


    @ApiOperation("局放数据分页数据")
    @PostMapping("tev/pageVo")
    public ResultJson<?> pageVo(@RequestBody TevVo tevVo) {
        IPage<TevVo> page = tevService.pageVo(tevVo);
        return ResultJson.success(page);
    }

    @ApiOperation("局放数据表格数据")
    @PostMapping("tev/charts")
    public ResultJson<?> chartsInfo(@RequestBody TevVo tevVo) {
        List<TevVo> records = tevService.pageVo(tevVo).getRecords();
        ChartsVo chartsVo = tevService.chartsInfo(records);
        return ResultJson.success(chartsVo);
    }


}
