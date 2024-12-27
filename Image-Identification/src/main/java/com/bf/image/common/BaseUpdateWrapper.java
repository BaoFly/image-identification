package com.bf.image.common;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

public class BaseUpdateWrapper<T> extends UpdateWrapper<T> {
    @Override
    protected String columnToString(String column) {
        // 驼峰命名转换为下划线命名
        return StringUtils.camelToUnderline(column);
    }
}