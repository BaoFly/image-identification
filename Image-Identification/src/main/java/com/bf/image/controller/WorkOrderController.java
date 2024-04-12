package com.bf.image.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bf.image.pojo.DetailInformation;
import com.bf.image.service.WorkOrderService;
import com.bf.image.vo.ResultJson;
import com.bf.image.vo.TevVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "工单接口")
@RestController
@CrossOrigin
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @ApiOperation("work order分页查询")
    @PostMapping("workOrder/pageVo")
    public ResultJson<?> pageVo(@RequestBody TevVo tevVo) {
        IPage<DetailInformation> page = workOrderService.pageVo(tevVo);
        return ResultJson.success(page);
    }

}
