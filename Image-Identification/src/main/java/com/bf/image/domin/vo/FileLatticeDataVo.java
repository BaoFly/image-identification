package com.bf.image.domin.vo;

import com.bf.image.entity.FileLatticeData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileLatticeDataVo extends FileLatticeData {

    private String previewUrl;

}
