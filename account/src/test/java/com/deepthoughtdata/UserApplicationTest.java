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
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.jws.soap.SOAPBinding;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @Author: jaysyd
 * @Date: 2018/4/27 15:09
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserApplicationTest {
    private MockMvc mvc;

    @Autowired
    private UserService userService;
    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() throws Exception{
       this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    //重置密码测试
    @Test
    public void rpasswd() throws Exception{
        String url = "/user/rpasswd";

        MvcResult mvcResult = this.mvc.perform(post(url)
                .param("email", "1046276907@qq.com")
                .param("password", "123455"))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        //接口返回结果
        String content = mvcResult.getResponse().getContentAsString();
        //打印结果和状态
        System.out.println("status = " + status);
        System.out.println("content = " + content);

        //断言预期结果和状态
        Assert.assertTrue("错误", status == 200);
        Assert.assertFalse("错误", status != 200);
    }

    //登录测试
    @Test
    public void login() throws Exception{
        String url = "/user/login";

        MvcResult mvcResult = this.mvc.perform(post(url)
                .param("email", "1046276907@qq.com")
                .param("password", "123455"))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        //接口返回结果
        String content = mvcResult.getResponse().getContentAsString();
        //打印结果和状态
        System.out.println("status = " + status);
        System.out.println("content = " + content);

        //断言预期结果和状态
        Assert.assertTrue("错误", status == 200);
        Assert.assertFalse("错误", status != 200);

    }

    //判断邮箱是否存在测试方法
    @Test
    public void exist() throws Exception{
        String url = "/user/exist";

        MvcResult mvcResult = this.mvc.perform(post(url)
                .param("email", "1046276907@qq.com")
                )
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        //接口返回结果
        String content = mvcResult.getResponse().getContentAsString();
        //打印结果和状态
        System.out.println("status = " + status);
        System.out.println("content = " + content);

        //断言预期结果和状态
        Assert.assertTrue("错误", status == 200);
        Assert.assertFalse("错误", status != 200);

    }

    //注册测试方法
    @Test
    public void register() throws Exception{
        String url = "/user/register";

        MvcResult mvcResult = this.mvc.perform(post(url)
                .param("email", "1046276907@qq.com")
                .param("password", "123455"))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        //接口返回结果
        String content = mvcResult.getResponse().getContentAsString();
        //打印结果和状态
        System.out.println("status = " + status);
        System.out.println("content = " + content);

        //断言预期结果和状态
        Assert.assertTrue("错误", status == 200);
        Assert.assertFalse("错误", status != 200);

    }

    //修改用户信息测试方法
    @Test
    public void updateUserInfo() throws Exception{
        String url = "/user/updateUserInfo";

        MvcResult mvcResult = this.mvc.perform(post(url)
                .param("id", "1")
                .param("email", "1211254@qq.com")
                .param("password", "123455"))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        //接口返回结果
        String content = mvcResult.getResponse().getContentAsString();
        //打印结果和状态
        System.out.println("status = " + status);
        System.out.println("content = " + content);

        //断言预期结果和状态
        Assert.assertTrue("错误", status == 200);
        Assert.assertFalse("错误", status != 200);

    }




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
