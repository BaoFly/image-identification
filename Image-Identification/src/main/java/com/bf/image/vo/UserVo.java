package com.bf.image.vo;

import com.bf.image.pojo.UserInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo extends UserInformation {

    private Integer current;

    private Integer pageSize;

    private List<Date> dateRange;

}
