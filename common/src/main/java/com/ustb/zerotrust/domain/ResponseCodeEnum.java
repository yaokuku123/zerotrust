package com.ustb.zerotrust.domain;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: ResponseCodeEnum
 * Author: yaoqijun
 * Date: 2021/5/25 14:08
 */
public enum ResponseCodeEnum {
    SUCCESS(20000, "成功"),
    FAIL(20001, "失败"),
    LOGIN_ERROR(1000, "用户名或密码错误"),
    PARAMETER_ILLEGAL(2001, "参数不合法");

    private final int code;
    private final String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
