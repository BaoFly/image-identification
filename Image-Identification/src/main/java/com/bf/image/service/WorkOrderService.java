package com.bf.image.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bf.image.pojo.WorkOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bf.image.vo.WorkOrderVo;

/**
* @author Gplus-033
* @description 针对表【work_order】的数据库操作Service
* @createDate 2024-04-12 09:36:10
*/
public interface WorkOrderService extends IService<WorkOrder> {

    IPage<WorkOrderVo> pageVo(WorkOrderVo workOrderVo);

    WorkOrderVo detail(WorkOrderVo workOrderVo);

    void create(WorkOrderVo workOrderVo);

    void review(WorkOrderVo workOrderVo);
}
