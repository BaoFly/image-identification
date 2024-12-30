package com.bf.image.domin.vo;

import com.bf.image.entity.ImageInformation;
import com.bf.image.entity.InspectionWorkOrder;
import com.bf.image.entity.TevInformation;
import com.bf.image.entity.UserInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TevVo extends TevInformation {

    private InspectionWorkOrder tevInspectionWorkOrder;

    private ImageInformation imageInformation;

    private String previewUrl;

}
