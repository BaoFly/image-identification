package com.bf.image.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.beans.Transient;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private T result;

    public BaseResult() {
        super();
    }

    private BaseResult(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    private BaseResult(int code, String msg, T result) {
        super();
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public static <T> BaseResult<T> failGenerator(String msg, T result) {
        BaseResult<T> baseResult = new BaseResult<T>(BaseReturnEnum.FAIL.getCode(), msg);
        baseResult.result = result;
        return baseResult;
    }

    public static <T> BaseResult<T> failGenerator(String msg) {
        BaseResult<T> baseResult = new BaseResult<T>(BaseReturnEnum.FAIL.getCode(), msg);
        return baseResult;
    }

    public static <T> BaseResult<T> failGenerator(int code, String msg) {
        BaseResult<T> baseResult = new BaseResult<T>(code, msg);
        return baseResult;
    }

    public static <T> BaseResult<T> failGenerator(int code, String msg, T result) {
        BaseResult<T> baseResult = new BaseResult<T>(code, msg, result);
        return baseResult;
    }

    public static <T> BaseResult<T> failGenerator(BaseReturnEnum baseReturnEnum) {
        BaseResult<T> baseResult = new BaseResult<T>(baseReturnEnum.getCode(), baseReturnEnum.getMessage());
        return baseResult;
    }

    public static <T> BaseResult<T> successGenerator(T result) {
        BaseResult<T> baseResult = new BaseResult<T>(BaseReturnEnum.SUCCESS.getCode(), BaseReturnEnum.SUCCESS.getMessage(), result);
        return baseResult;
    }

    public static <T> BaseResult<T> successGenerator(String msg, T result) {
        BaseResult<T> baseResult = new BaseResult<T>(BaseReturnEnum.SUCCESS.getCode(), msg, result);
        return baseResult;
    }

    public static <T> BaseResult<T> customGenerator(int code, String msg, T result) {
        BaseResult<T> baseResult = new BaseResult<T>(code, msg, result);
        return baseResult;
    }

    /**
     *
     * @param resultFlag  结果标识 false失败 true成功
     * @param successMsg  成功返回的message
     * @param failedCode  失败时返回的code
     * @param failedMsg   失败时返回的message
     * @param result      成功结果
     * @return
     */
    public static <T> BaseResult<T> booleanGenerator(boolean resultFlag, String successMsg,
        int failedCode, String failedMsg, T result) {
        BaseResult<T> baseResult = resultFlag ? successGenerator(successMsg, result)
            : failGenerator(failedCode, failedMsg);
        return baseResult;
    }

    /**
     *
     * @param resultFlag   结果标识 false失败 true成功
     * @param failedMsg    失败时返回的message
     * @param result       成功结果
     * @return
     */
    public static <T> BaseResult<T> defaultBooleanGenerator(boolean resultFlag, String failedMsg,
        T result) {
        BaseResult<T> baseResult = booleanGenerator(resultFlag,
            BaseReturnEnum.SUCCESS.getMessage(), BaseReturnEnum.FAIL.getCode(), failedMsg, result);
        return baseResult;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

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

    public T getResult(Class<?> clazz) {
        if (result instanceof LinkedHashMap) {
            result = (T) jsonParser.convertValue(result, clazz);
        }
        if (result instanceof ArrayList) {
            CollectionType listType = jsonParser.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            result = jsonParser.convertValue(result, listType);
        }
        return result;
    }

    public Page getPageResult(Class<?> clazz) {
        Page page = jsonParser.convertValue(result, Page.class);
        CollectionType listType = jsonParser.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
        List<T> list = jsonParser.convertValue(page.getRecords(), listType);
        page.setRecords(list);
        return page;
    }

    @Transient
    public boolean isSuccess() {
        return BaseReturnEnum.SUCCESS.getCode() == code;
    }

    @Transient
    public boolean isFail() {
        return BaseReturnEnum.SUCCESS.getCode() != code;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
            "code=" + code +
            ", msg='" + msg + '\'' +
            ", result=" + result +
            '}';
    }
}
