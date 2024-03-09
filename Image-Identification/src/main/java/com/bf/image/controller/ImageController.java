package com.bf.image.controller;

import com.bf.image.service.ImageInformationService;
import com.bf.image.service.MinIOUService;
import com.bf.image.vo.FileVo;
import com.bf.image.vo.ResultJson;
import io.swagger.annotations.Api;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "imageInformation相关接口")
@RestController
@CrossOrigin
public class ImageController {

    @Autowired
    private ImageInformationService imageService;

    @Autowired
    private MinIOUService minIOUService;



    @PostMapping("image/remove")
    public ResultJson removeImage(@RequestBody FileVo fileVo) {
        imageService.removeRecord(fileVo);
        return ResultJson.success();
    }
}
