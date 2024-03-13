package com.bf.image.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeriesData {

    /**
     * 折线名称
     */
    private String name;

    @JsonProperty(defaultValue = "line") // 设置默认值为 "line"
    private String type;

    @JsonProperty(defaultValue = "Total") // 设置默认值为 "Total"
    private String stack;


    private List<Double> data;

}
