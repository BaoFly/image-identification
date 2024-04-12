package com.bf.image.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bf.image.pojo.DetailInformation;
import com.bf.image.pojo.FileUploadBody;
import com.bf.image.service.ImageInformationService;
import com.bf.image.service.MinIOUService;
import com.bf.image.service.TevInformationService;
import com.bf.image.vo.*;
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
    private TevInformationService tevService;

    @Autowired
    private MinIOUService minIOUService;

    @Autowired
    private ImageInformationService imageService;


    @ApiOperation("tev分页查询")
    @PostMapping("tev/pageVo")
    public ResultJson<?> pageVo(@RequestBody TevVo tevVo) {
        IPage<TevVo> page = tevService.pageVo(tevVo);
        return ResultJson.success(page);
    }

    @ApiOperation("tev表格信息")
    @PostMapping("tev/charts")
    public ResultJson<?> chartsInfo(@RequestBody TevVo tevVo) {
        List<TevVo> records = tevService.pageVo(tevVo).getRecords();
        ChartsVo chartsVo = tevService.chartsInfo(records);
        return ResultJson.success(chartsVo);
    }

    @ApiOperation("tev上传所有相关信息")
    @PostMapping("tev/save")
    public ResultJson saveDetailInfo(@RequestBody TevVo tevVo) {
        tevService.saveRecord(tevVo);
        return ResultJson.success();
    }


}
