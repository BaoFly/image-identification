package com.bf.image.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.bf.image.pojo.DetailInformation;
import com.bf.image.pojo.DeviceInformation;
import com.bf.image.pojo.ImageInformation;
import com.bf.image.pojo.UserInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailInformationVo extends DetailInformation{

    private Integer current;

    private Integer pageSize;

    private List<Date> dateRange;

    private String imageId;

}
