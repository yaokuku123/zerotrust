package com.ustb.zerotrust.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: ResponseResult
 * Author: yaoqijun
 * Date: 2021/5/25 14:10
 */
public class ResponseResult implements Serializable {
    private Boolean success; //是否成功
    private Integer code; //返回码
    private String msg; //消息
    private Map<String, Object> data = new HashMap<>(); //数据

    private ResponseResult() {
    }

    public static ResponseResult success() {
        ResponseResult r = new ResponseResult();
        r.setSuccess(true);
        r.setCode(ResponseCodeEnum.SUCCESS.getCode());
        r.setMsg(ResponseCodeEnum.SUCCESS.getMessage());
        return r;
    }

    public static ResponseResult error() {
        ResponseResult r = new ResponseResult();
        r.setSuccess(false);
        r.setCode(ResponseCodeEnum.FAIL.getCode());
        r.setMsg(ResponseCodeEnum.FAIL.getMessage());
        return r;
    }

    public ResponseResult success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public ResponseResult message(String msg){
        this.setMsg(msg);
        return this;
    }

    public ResponseResult code(Integer code){
        this.setCode(code);
        return this;
    }

    public ResponseResult data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public ResponseResult data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
