package com.bf.image.domin.vo;

import com.bf.image.entity.InspectionWorkOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InspectionWorkOrderVo extends InspectionWorkOrder {

    private Object data;

}
