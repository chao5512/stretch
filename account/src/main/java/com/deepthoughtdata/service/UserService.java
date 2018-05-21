package com.deepthoughtdata.service;

import com.deepthoughtdata.dao.UserRepository;
import com.deepthoughtdata.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.soap.SOAPBinding;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: jaysyd
 * @Date: 2018/4/27 17:01
 * @Description:
 */
public interface UserService {



    /**
     * 功能描述:保存用户，将用户的密码用MD5+salt进行加密，并保存salt
     * @param user
     * @return: com.deepthoughtdata.entity.User
     * @auther: 王培文
     * @date: 2018/5/15 17:36
     */
    public User save(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    //根据email查找用户信息
    public User findByEmailAndStatus(String email, Long status);

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
    /**
     * 功能描述:将密码进行MD5+salt校验
     * @param email
     * @param password
     * @return: com.deepthoughtdata.entity.User
     * @auther: 王培文
     * @date: 2018/5/15 18:05
     */
    public User findByEmailAndPassword(String email, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException;



    public int modifyByEmailAndPasswordAndSalt(String email, String password, String salt);

    public int modifyUserInfo(User user);
}
