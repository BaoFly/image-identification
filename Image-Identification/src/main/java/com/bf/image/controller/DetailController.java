package com.bf.image.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.config.MinIOConfig;
import com.bf.image.constant.CommonConstant;
import com.bf.image.exception.CustomException;
import com.bf.image.pojo.DetailInformation;
import com.bf.image.pojo.FileUploadBody;
import com.bf.image.pojo.ImageInformation;
import com.bf.image.service.DetailInformationService;
import com.bf.image.service.ImageInformationService;
import com.bf.image.service.MinIOUService;
import com.bf.image.vo.ChartsVo;
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
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Api(tags = "detailInformation相关接口")
@RestController
@CrossOrigin
public class DetailController {

    @Autowired
    private DetailInformationService detailService;

    @Autowired
    private MinIOUService minIOUService;

    @Autowired
    private MinIOConfig minIOConfig;

    @Autowired
    private ImageInformationService imageService;

    @ApiOperation("下载图片")
    @PostMapping("detail/download")
    public void downloadImage(@RequestParam("fileNameList") ArrayList<String> fileNameList,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
//       minIOUService.batchDownload(
//                fileNameList,
//                String.valueOf(System.currentTimeMillis()),
//                response,
//                request
//        );
        minIOUService.downloadFile(
                response,
                fileNameList.get(0),
                minIOConfig.getBucketName()
        );
    }

    @ApiOperation("下载图片")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("detail/downloadFile")
    public void downloadImage(String fileName,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        minIOUService.downloadFile(
                response,
                fileName,
                minIOConfig.getBucketName()
        );
    }

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

    @ApiOperation("表格信息")
    @PostMapping("detail/charts")
    public ResultJson<?> chartsInfo(@RequestBody DetailInformationVo detailInformationVo) {
        List<DetailInformation> records = detailService.pageVo(detailInformationVo).getRecords();
        ChartsVo chartsVo = detailService.chartsInfo(records);
        return ResultJson.success(chartsVo);
    }

}
