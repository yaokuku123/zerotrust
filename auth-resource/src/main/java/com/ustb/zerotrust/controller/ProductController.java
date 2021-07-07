package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: ProductController
 * Author: yaoqijun
 * Date: 2021/2/25 11:20
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @PostMapping("/findAll")
    public ResponseResult findAll(HttpServletRequest request) {
        String params = ReadAsChars(request);
        return ResponseResult.success().data("result","9002 success findAll..." + params);
    }

    /**
     * 获取请求body的信息
     * @param request
     * @return
     */
    private static String ReadAsChars(HttpServletRequest request)
    {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
