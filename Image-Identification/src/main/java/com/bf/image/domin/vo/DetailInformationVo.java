package com.bf.image.domin.vo;

import com.bf.image.entity.DetailInformation;
import com.bf.image.entity.FileLatticeData;
import com.bf.image.entity.ImageInformation;
import com.bf.image.entity.InspectionWorkOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailInformationVo extends DetailInformation {

    private InspectionWorkOrder detailInspectionWorkOrder;

    private ImageInformation imageInformation;

    private String previewUrl;

    private List<FileLatticeDataVo> fileLatticeDataVoList;

}
