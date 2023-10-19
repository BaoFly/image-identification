package com.bf.image.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bf.image.pojo.DetailInformation;
import com.bf.image.service.DetailInformationService;
import com.bf.image.vo.DetailInformationVo;
import com.bf.image.vo.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/info")
public class DetailController {

    @Autowired
    private DetailInformationService detailService;

    @PostMapping("/upload")
    public ResultJson uploadInfo(@RequestBody DetailInformation detailInformation,
                                 HttpServletRequest request) {
        ServletContext servletContext = request.getServletContext();
        String fullUrl = servletContext.getRealPath("/");
        detailService.uploadInfo(detailInformation, fullUrl);
        return ResultJson.success();
    }

    @GetMapping("/newestInfo")
    public ResultJson<DetailInformation> getNewestInfo(@RequestBody DetailInformation detailInformation) {
        detailInformation = detailService.getNewestInfo(detailInformation);
        return ResultJson.success(detailInformation);
    }

    @GetMapping("/condition")
    public ResultJson<List<DetailInformation>> getDetailInfoByCondition(@RequestBody DetailInformationVo detailInformationVo) {
        List<DetailInformation> detailInfoPage = detailService.getDetailInfoByCondition(detailInformationVo);
        return ResultJson.success(detailInfoPage);
    }

}
