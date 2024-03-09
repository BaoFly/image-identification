package com.bf.image.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.constant.CommonConstant;
import com.bf.image.exception.CustomException;
import com.bf.image.pojo.DetailInformation;
import com.bf.image.pojo.FileUploadBody;
import com.bf.image.service.DetailInformationService;
import com.bf.image.service.ImageInformationService;
import com.bf.image.service.MinIOUService;
import com.bf.image.vo.DetailInformationVo;
import com.bf.image.vo.FileVo;
import com.bf.image.vo.ResultJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Api(tags = "detailInformation相关接口")
@RestController
@CrossOrigin
public class DetailController {

    @Autowired
    private DetailInformationService detailService;

    @Autowired
    private MinIOUService minIOUService;

    @Autowired
    private ImageInformationService imageService;

    @ApiOperation("上传图片")
    @PostMapping("detail/upload")
    public ResultJson uploadInfo(FileUploadBody body) {
        List<FileVo> list = minIOUService.uploadFile(body);
        imageService.saveImageByFileVo(list);
        return Objects.isNull(body.getFiles()) ? ResultJson.fail("上传文件不能为空") : ResultJson.success(list,"上传成功");
    }

    @ApiOperation("上传所有相关信息 upload detail")
    @PostMapping("detail/save")
    public ResultJson saveDetailInfo(@RequestBody DetailInformationVo detailInformationVo) {
        detailService.saveRecord(detailInformationVo);
        return ResultJson.success();
    }

    @ApiOperation("分页查询")
    @PostMapping("detail/pageVo")
    public ResultJson<?> pageVo(@RequestBody DetailInformationVo detailInformationVo) {
        System.out.println(JSONObject.toJSONString(detailInformationVo));
        IPage<DetailInformation> page = detailService.pageVo(detailInformationVo);
        return ResultJson.success(page);
    }

}
