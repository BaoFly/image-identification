package com.bf.image.common;

/**
 * 返回码枚举类
 */
public enum BaseReturnEnum {

    // 公共返回码10开头
    SUCCESS(1, "success"),
    FAIL(0, "fail"),
    UNKNOWN_ERROR(10999, "unknownError"),
    LOGIN_FAIL(-1, "loginFail"),
    SYSTEM_ERROR(99999, "systemError"),
    // 不对外使用
    PARAMETER_ERROR(88888, "parameterProblem"),
    //更新参数错误
    BASE_QUERY_PARAMETER_ERROR(66666, "baseQueryParameterError"),
    //参数错误专用
    PARAM_ERROR(33333, "parameterError"),
    BUSINESS_ERROR(77777, "businessProblem"),
    CODE_ERROR(-2, "verificationCodeError"),
    PHONE_NOT_EXIST_ERROR(-4, "phoneNumberError"),
    PHONE_EXIST_ERROR(-5, "phoneExistError"),
    PASSWORD_FORMAT_ERROR(-3, "passwordFormatError"),
    PASSWORD_ERROR(-6, "passwordError"),
    REPEAT_SUBMIT_ERROR(-7, "repeatSubmitError"),
    CHECK_PERMISSIONS_ERROR(-8, "checkPermissionsError"),
    //熔断编码
    FUSE_CODE(-99, "fuseCode");

    private int code;
    private String message;

    BaseReturnEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
