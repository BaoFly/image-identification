package com.bf.image.common;

import lombok.Data;

/**
 * 异常类
 */
@Data
public class BaseException extends RuntimeException {

    private BaseReturnEnum baseReturnEnum;

    private String msg;

    private int code;

    private Object result;

    public BaseException(BaseReturnEnum baseReturnEnum) {
        this.baseReturnEnum = baseReturnEnum;
    }

    public BaseException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseException(BaseReturnEnum baseReturnEnum, String extraMsg) {
        this.baseReturnEnum = baseReturnEnum;
        this.msg = extraMsg;
    }

    public BaseException(int code, Object result) {
        this.code = code;
        this.result = result;
    }

    public BaseException(int code, String msg, Object result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public static BaseException build(BaseReturnEnum baseReturnEnum) {
        return new BaseException(baseReturnEnum);
    }

    public static BaseException build(int code, String msg) {
        return new BaseException(code, msg);
    }

    public static BaseException build(int code, Object result) {
        return new BaseException(code, result);
    }
}
