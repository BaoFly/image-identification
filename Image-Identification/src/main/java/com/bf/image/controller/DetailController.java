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
import java.util.Objects;

@RestController
@RequestMapping("/info")
public class DetailController {

    @Autowired
    private DetailInformationService detailService;

    @PostMapping("/upload")
    public ResultJson uploadInfo(@RequestBody DetailInformationVo detailInformationVo,
                                 HttpServletRequest request) {
        if (Objects.isNull(detailInformationVo)) {
            return ResultJson.success(null);
        }
        ServletContext servletContext = request.getServletContext();
        String fullUrl = servletContext.getRealPath("/");
        DetailInformation detailInformation = detailService.convert(detailInformationVo);
        detailService.uploadInfo(detailInformation, fullUrl);
        return ResultJson.success();
    }

    @GetMapping("/newestInfo")
    public ResultJson<DetailInformation> getNewestInfo(@RequestBody DetailInformationVo detailInformationVo) {
        if (Objects.isNull(detailInformationVo)) {
            return ResultJson.success(null);
        }
        DetailInformation detailInformation = detailService.convert(detailInformationVo);
        return ResultJson.success(detailService.getNewestInfo(detailInformation));
    }

    @GetMapping("/condition")
    public ResultJson<List<DetailInformation>> getDetailInfoByCondition(@RequestBody DetailInformationVo detailInformationVo) {
        if (Objects.isNull(detailInformationVo)) {
            return ResultJson.success(detailService.getAllRecord());
        }
        List<DetailInformation> detailInfoPage = detailService.getDetailInfoByCondition(detailInformationVo);
        return ResultJson.success(detailInfoPage);
    }

}
