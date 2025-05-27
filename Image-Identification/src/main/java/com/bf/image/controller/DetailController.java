package com.bf.image.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bf.image.config.MinIOConfig;
import com.bf.image.entity.FileUploadBody;
import com.bf.image.service.impl.DetailInformationServiceImpl;
import com.bf.image.service.impl.FileLatticeDataServiceImpl;
import com.bf.image.service.impl.ImageInformationServiceImpl;
import com.bf.image.service.impl.MinIOUServiceImpl;
import com.bf.image.domin.vo.ChartsVo;
import com.bf.image.domin.vo.DetailInformationVo;
import com.bf.image.domin.vo.FileVo;
import com.bf.image.domin.ResultJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Api(tags = "detailInformation相关接口")
@RestController
@CrossOrigin
public class DetailController {

    @Autowired
    private DetailInformationServiceImpl detailService;

    @Autowired
    private MinIOUServiceImpl minIOUService;

    @Autowired
    private MinIOConfig minIOConfig;

    @Autowired
    private ImageInformationServiceImpl imageService;

    @Autowired
    private FileLatticeDataServiceImpl latticeDataService;

    @ApiOperation("下载图片")
    @PostMapping("detail/download")
    public void downloadImage(@RequestParam("fileNameList") ArrayList<String> fileNameList,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
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
    @Transactional
    public ResultJson uploadInfo(FileUploadBody body) {
        List<FileVo> list = minIOUService.uploadFile(body);
        imageService.saveImageByFileVo(list);
        return Objects.isNull(body.getFiles()) ? ResultJson.fail("上传文件不能为空") : ResultJson.success(list,"上传成功");
    }

    @ApiOperation("上传点阵文件")
    @PostMapping("detail/uploadTxt")
    public ResultJson uploadTxt(FileUploadBody body) {
        List<FileVo> list = minIOUService.uploadFile(body);
        latticeDataService.saveTxtByFileVo(list, body.getDataId(), body.getType());
        return Objects.isNull(body.getFiles()) ? ResultJson.fail("上传文件不能为空") : ResultJson.success(list,"上传成功");
    }

    @ApiOperation("下载点阵文件")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("detail/downloadTxt")
    public void downloadTxt(String fileName,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        minIOUService.downloadFile(
                response,
                fileName,
                minIOConfig.getBucketName()
        );
    }

    @ApiOperation("红外图像数据分页查询")
    @PostMapping("detail/pageVo")
    public ResultJson<?> pageVo(@RequestBody String queryData) {
        IPage<DetailInformationVo> page = detailService.pageVo(queryData);
        return ResultJson.success(page);
    }

    @ApiOperation("表红外图像数据表格信息")
    @PostMapping("detail/charts")
    public ResultJson<?> chartsInfo(@RequestBody String queryData) {
        List<DetailInformationVo> records = detailService.pageVo(queryData).getRecords();
        ChartsVo chartsVo = detailService.chartsInfo(records);
        return ResultJson.success(chartsVo);
    }

}
