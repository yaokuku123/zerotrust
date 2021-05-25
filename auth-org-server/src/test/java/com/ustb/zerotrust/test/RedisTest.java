package com.ustb.zerotrust.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: RedisTest
 * Author: yaoqijun
 * Date: 2021/5/25 15:14
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("name","yorick");
        System.out.println(redisTemplate.opsForValue().get("name"));
    }
}