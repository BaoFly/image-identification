package com.bf.image.utils;

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

    /**
     * 将JSON字符串反序列化为指定类型的实体对象
     *
     * @param json  JSON字符串
     * @param clazz 目标实体类的Class对象
     * @param <T>   实体类类型
     * @return 反序列化后的实体对象
     * @throws JsonProcessingException 当JSON格式错误或反序列化失败时抛出
     */
    public static <T> T parseJsonToEntity(String json, Class<T> clazz) throws JsonProcessingException {
        if (json == null || json.isEmpty()) {
            throw new IllegalArgumentException("JSON字符串不能为null或空");
        }
        if (clazz == null) {
            throw new IllegalArgumentException("目标类Class不能为null");
        }
        // 使用ObjectMapper将JSON字符串转换为指定类型的对象
        return jsonParser.readValue(json, clazz);
    }

    /**
     * data为clazz的继承类，返回增强类
     * @param data
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> T convertValue(Object data, Class<T> clazz) {
        return jsonParser.convertValue(data, clazz);
    }

    public static <T> List<T> getConvertList(String jsonString, Class<T> clazz) {
        CollectionType listType = jsonParser.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
        List<T> list = null;
        try {
            list = jsonParser.readValue(jsonString, listType);
        } catch (Exception e) {
            throw new RuntimeException("转换出现异常");
        }
        return list;
    }

    /**
     * 将JSON字符串转换为指定类型的对象列表
     *
     * @param sourceList 待转换的JSON字符串（格式应为JSON数组）
     * @param targetClass 列表中元素的类型Class对象（如User.class）
     * @param <T> 列表元素的泛型类型
     * @return 转换后的List<T>对象列表，若转换失败则抛出RuntimeException
     */
    public static <T> List<T> convertToList(List sourceList, Class<T> targetClass) {
        CollectionType listType = jsonParser.getTypeFactory().constructCollectionType(ArrayList.class, targetClass);
        return jsonParser.convertValue(sourceList, listType);
    }
}
