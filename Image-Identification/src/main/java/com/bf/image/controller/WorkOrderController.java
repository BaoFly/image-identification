package com.bf.image.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bf.image.pojo.DetailInformation;
import com.bf.image.pojo.WorkOrder;
import com.bf.image.service.WorkOrderService;
import com.bf.image.vo.ResultJson;
import com.bf.image.vo.TevVo;
import com.bf.image.vo.WorkOrderVo;
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
    public ResultJson<?> pageVo(@RequestBody WorkOrderVo workOrderVo) {
        IPage<WorkOrderVo> page = workOrderService.pageVo(workOrderVo);
        return ResultJson.success(page);
    }

    @ApiOperation("work order详情审核查看页面")
    @PostMapping("workOrder/detail")
    public ResultJson<?> detail(@RequestBody WorkOrderVo workOrderVo) {
        WorkOrderVo vo = workOrderService.detail(workOrderVo);
        return ResultJson.success(vo);
    }

    @ApiOperation("work order创建工单")
    @PostMapping("workOrder/create")
    public ResultJson<?> create(@RequestBody WorkOrderVo workOrderVo) {
        workOrderService.create(workOrderVo);
        return ResultJson.success();
    }

    @ApiOperation("work order审核工单")
    @PostMapping("workOrder/review")
    public ResultJson<?> review(@RequestBody WorkOrderVo workOrderVo) {
        workOrderService.review(workOrderVo);
        return ResultJson.success();
    }

}
