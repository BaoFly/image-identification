package com.bf.image.controller;

import com.bf.image.service.impl.ImageInformationServiceImpl;
import com.bf.image.service.impl.MinIOUServiceImpl;
import com.bf.image.domin.vo.FileVo;
import com.bf.image.domin.ResultJson;
import io.swagger.annotations.Api;
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
    private ImageInformationServiceImpl imageService;

    @Autowired
    private MinIOUServiceImpl minIOUService;

    @PostMapping("image/remove")
    public ResultJson removeImage(@RequestBody FileVo fileVo) {
        imageService.removeRecord(fileVo);
        return ResultJson.success();
    }
}
