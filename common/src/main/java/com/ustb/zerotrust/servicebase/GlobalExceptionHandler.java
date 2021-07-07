package com.ustb.zerotrust.servicebase;

import com.ustb.zerotrust.domain.ResponseCodeEnum;
import com.ustb.zerotrust.domain.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult error(Exception e){
        e.printStackTrace();
        return ResponseResult.error();
    }
}
