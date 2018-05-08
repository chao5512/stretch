package com.deepthoughtdata.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.deepthoughtdata.dao.UserRepository;
import com.deepthoughtdata.entity.User;
import com.deepthoughtdata.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

/**
 * @Author: jaysyd
 * @Date: 2018/4/27 17:03
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService{

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${spring.mail.username}")
    public String mailUsername;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void userValidate(User user, String code) {
        MimeMessage message = javaMailSender.createMimeMessage();


        try{
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(mailUsername);
            helper.setTo(user.getEmail());
            helper.setSubject("用户注册（邮件主题）");
            String link = "localhost:8888/user/registered?mailcode=" + code;
//            String messages = String.format(link);
//            helper.setText(messages, true);
//            System.out.println("messages=" + messages);
            message.setContent("尊敬的用户，您好！我是宋远迪，请点击激活链接完成邮箱激活<a href="
                    + link + ">" + link +"</a>", "text/html;charset=UTF-8");
            javaMailSender.send(message);


        }catch (MessagingException e){
            logger.error("发送邮件失败：User:" + JSONObject.toJSONString(user) + ", mailcode: " + code);
        }

    }

    @Override
    public User findByToken(String code) {
        return userRepository.findAllByToken(code);
    }

    @Override
    public int modifyByToken(String token, Long status) {
        return userRepository.modifyByTokenAndStatus(token, status);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }


}
