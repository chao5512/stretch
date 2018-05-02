package com.deepthoughtdata;

import com.alibaba.fastjson.JSONObject;
import com.deepthoughtdata.controller.UserContoller;
import com.deepthoughtdata.entity.User;
import com.deepthoughtdata.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.jws.soap.SOAPBinding;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @Author: jaysyd
 * @Date: 2018/4/27 15:09
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserApplicationTest {
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception{
       mvc = MockMvcBuilders.standaloneSetup(new UserContoller()).build();
    }

//    @Test
//    public void toLogin() throws Exception{
//        String url = "/user/toLogin";
//        String expectedResult = "login";//预期返回结果
//        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//        int status = mvcResult.getResponse().getStatus();
//        //接口返回结果
//        String content = mvcResult.getResponse().getContentAsString();
//        //打印结果和状态
//        System.out.println("status = " + status);
//        System.out.println("content = " + content);
//
//        //断言预期结果和状态
//        Assert.assertTrue("错误", status == 200);
//        Assert.assertFalse("错误", status != 200);
//        Assert.assertTrue("数据一致", expectedResult.equals(content));
//        Assert.assertFalse("数据不一致", !expectedResult.equals(content));
//
//    }
//
//    @Test
//    public void login() throws Exception{
//        String url = "/user/login";
//        String expectedResult = "login";//预期返回结果
//        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//        int status = mvcResult.getResponse().getStatus();
//        //接口返回结果
//        String content = mvcResult.getResponse().getContentAsString();
//        //打印结果和状态
//        System.out.println("status = " + status);
//        System.out.println("content = " + content);
//
//        //断言预期结果和状态
//        Assert.assertTrue("错误", status == 200);
//        Assert.assertFalse("错误", status != 200);
//        Assert.assertTrue("数据一致", expectedResult.equals(content));
//        Assert.assertFalse("数据不一致", !expectedResult.equals(content));
//
//    }
//    @Test
//    public void registerTest(){
//        User user = userService.findByEmail("ad");
//        assert user == null;
//    }
    @Test
    public void UUIDTest() throws NoSuchAlgorithmException {
        User user = new User();
        user.setEmail("11111");
        user.setUsername("adsa");
        String token = UUID.randomUUID().toString();
        String value = JSONObject.toJSONString(user);
        token = token + value;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(token.getBytes());
        String mdsStr = new BigInteger(1,md.digest()).toString(16);

        System.out.println(token);
    }



}
