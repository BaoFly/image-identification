package com.bf.image.utils;

import com.bf.image.vo.TevVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static final ObjectMapper jsonParser;

    static {
        jsonParser = new ObjectMapper();
        SimpleFilterProvider provider = new SimpleFilterProvider();
        provider.setFailOnUnknownId(false);
        jsonParser.setFilterProvider(provider);
        SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonParser.setDateFormat(smt);
        //fix 11-5 解决服务新增字段后，序列化出现问题导致大面积服务崩溃。
        jsonParser.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        jsonParser.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    public static <T> List<T> getConvertList(String jsonString, Class<T> clazz) {
        CollectionType listType = jsonParser.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
        List<T> list = null;
        try {
            list = jsonParser.readValue(jsonString, listType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("转换出现异常");
        }
        return list;
    }

    public static <T> List<T> convertToList(List sourceList, Class<T> targetClass) {
        CollectionType listType = jsonParser.getTypeFactory().constructCollectionType(ArrayList.class, targetClass);
        return jsonParser.convertValue(sourceList, listType);
    }
}
