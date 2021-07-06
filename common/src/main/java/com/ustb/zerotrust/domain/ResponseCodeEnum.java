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
    UNKNOWN_ERROR(2000, "未知错误"),
    PARAMETER_ILLEGAL(2001, "参数不合法"),
    TOKEN_INVALID(2002, "无效的Token"),
    TOKEN_SIGNATURE_INVALID(2003, "无效的签名"),
    TOKEN_EXPIRED(2004, "token已过期"),
    TOKEN_MISSION(2005, "token缺失"),
    REFRESH_TOKEN_INVALID(2006, "刷新Token无效");

    private int code;
    private String message;

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
