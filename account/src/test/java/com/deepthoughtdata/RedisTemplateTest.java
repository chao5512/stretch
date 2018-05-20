package com.deepthoughtdata;

import com.deepthoughtdata.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.concurrent.TimeUnit;


@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTemplateTest {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Test
    public void testPut() {
        redisTemplate.opsForValue().set("aa", "bb",50 ,TimeUnit.SECONDS);
    }
    @Test
    public void testGet() {
        System.out.println(redisTemplate.opsForValue().get("aa"));
    }
}
