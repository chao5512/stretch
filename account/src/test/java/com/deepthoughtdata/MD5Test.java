package com.deepthoughtdata;

import com.deepthoughtdata.dao.UserRepository;
import com.deepthoughtdata.entity.User;
import com.deepthoughtdata.service.UserService;
import com.deepthoughtdata.util.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MD5Test {
    @Autowired
    private UserService userService;
    /**
     * 功能描述:测试MD5+salt是否可用
     * @param 
     * @return: void
     * @auther: 王培文
     * @date: 2018/5/15 17:48
     */
    @Test
    public void testPwd(){
        User user = new User();
        user.setEmail("186231@qq.com");
        user.setUsername("zs1");
        user.setPassword("123456");
        try {
            userService.save(user);
            System.out.println("注册成功。。。。");
            User user1 = userService.findByEmailAndPassword("186231@qq.com","12345");
            System.out.println("id："+user1.getId());
            System.out.println("用户名："+user1.getUsername());
            System.out.println("密码："+user1.getPassword());
            System.out.println("email："+user1.getEmail());
            System.out.println("头像路径："+user1.getImagePath());
            System.out.println("salt："+user1.getSalt());
            System.out.println("状态："+user1.getStatus());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        /*String salt = MD5Util.createSalt();
        System.out.println("盐值："+salt);
        try {
            //模拟注册
            String pwdIndb = MD5Util.encode("123456", salt);
            System.out.println("数据库中密码："+pwdIndb);
            //模拟登录
            String rawPwd = "123456";
            boolean result = MD5Util.isPasswordValid(pwdIndb,rawPwd,salt);
            System.out.println("登录状态："+result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/

    }
}
