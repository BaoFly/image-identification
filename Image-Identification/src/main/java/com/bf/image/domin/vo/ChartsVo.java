package com.bf.image.domin.vo;

import com.bf.image.domin.SeriesData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChartsVo {

    /**
     * 类别 -> 横坐标
     */
    private List<String> categories;

    /**
     * 每一种类别的数据
     */
    private List<SeriesData> seriesDataList;

}
