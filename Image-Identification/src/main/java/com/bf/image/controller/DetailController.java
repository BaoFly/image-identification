package com.bf.image.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.constant.CommonConstant;
import com.bf.image.exception.CustomException;
import com.bf.image.pojo.DetailInformation;
import com.bf.image.service.DetailInformationService;
import com.bf.image.vo.DetailInformationVo;
import com.bf.image.vo.ResultJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Api(tags = "detailInformation相关接口")
@RestController
@CrossOrigin
public class DetailController {

    @Autowired
    private DetailInformationService detailService;

    @ApiOperation("上传所有相关信息 upload detail")
    @PostMapping("/upload")
    public ResultJson uploadInfo(@RequestBody DetailInformationVo detailInformationVo,
                                 HttpServletRequest request) {
        if (ObjectUtils.allNull(detailInformationVo)) {
            return ResultJson.fail("值不能全为0");
        }
        ServletContext servletContext = request.getServletContext();
        String fullUrl = servletContext.getRealPath("/");
        detailService.uploadInfo(detailInformationVo, fullUrl);
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
