package com.bf.image.domin.vo;

import com.bf.image.entity.DetailInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailInformationVo extends DetailInformation {

    private Integer current;

    private Integer pageSize;

    private List<Date> dateRange;


}
