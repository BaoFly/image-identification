package com.bf.image.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.text.SimpleDateFormat;

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
}
