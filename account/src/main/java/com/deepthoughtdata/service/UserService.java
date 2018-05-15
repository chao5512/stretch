package com.deepthoughtdata.service;

import com.deepthoughtdata.dao.UserRepository;
import com.deepthoughtdata.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Author: jaysyd
 * @Date: 2018/4/27 17:01
 * @Description:
 */
public interface UserService {



    //增加用户
    public User save(User user) ;

    //根据email查找用户信息
    public User findByEmail(String email);

    //根据username查找用户信息
    public User findByUsername(String username);

    //邮件发送服务
    public void userValidate(User user, String link);

    public User findByToken(String code);

    public int modifyByToken(String token, Long status);

    /**
     * 功能描述: 文件上传
     *
     * @param: [file, user]
     * @return: void
     * @auther: 王培文
     * @date: 2018/5/10 19:27
     */
    public void fileUpload(MultipartFile file,User user) throws IOException;

    /**
     * 功能描述:根据id查询用户
     * @param id
     * @return: user
     * @auther: 王培文
     * @date: 2018/5/15 10:15
     */
    public User findByUserId(long id);
    public User findByEmailAndPassword(String email, String password);

    public int modifyByEmailAndPassword(String email, String password);
}
