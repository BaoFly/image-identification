package com.bf.image.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.entity.InspectionWorkOrder;
import com.bf.image.mapper.InspectionWorkOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Gplus-033
* @description 针对表【inspection_work_order】的数据库操作Service实现
* @createDate 2024-12-27 14:50:13
*/
@Service
public class InspectionWorkOrderServiceImpl extends ServiceImpl<InspectionWorkOrderMapper, InspectionWorkOrder>
    implements IService<InspectionWorkOrder> {

    @Autowired
    private InspectionWorkOrderMapper inspectionWorkOrderMapper;

    public List<InspectionWorkOrder> getList(LambdaQueryWrapper queryWrapper) {
        return inspectionWorkOrderMapper.selectList(queryWrapper);
    }

}




