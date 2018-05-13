package com.deepthoughtdata.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.deepthoughtdata.dao.UserRepository;
import com.deepthoughtdata.entity.User;
import com.deepthoughtdata.service.UserService;
import com.deepthoughtdata.util.Upload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;

/**
 * @Author: jaysyd
 * @Date: 2018/4/27 17:03
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${spring.mail.username}")
    public String mailUsername;

    @Value("${file.address}")
    public String fileAddress;

    @Value("${server.ip}:${server.port}")
    private String mailUri;

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

    //发送邮件
    @Override
    public void userValidate(User user,  String link) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(mailUsername);
            helper.setTo(user.getEmail());
            helper.setSubject("用户注册（邮件主题）");
            String strText = "尊敬的用户，您好！我是宋远迪，请点击激活链接完成邮箱激活<p><a href='"
                    + link + "' target='_blank'>" + "激活账号" +"</a></p>";
            helper.setText(strText, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
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
    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public int modifyByEmailAndPassword(String email, String password) {
        return userRepository.modifyByEmailAndPassword(email, password);
    }

    /**
     * 功能描述:文件上传实现类
     *
     * @param: [file, user]
     * @return: void
     * @auther: 王培文
     * @date: 2018/5/10 19:28
     */
    @Override
    @Transactional
    public void fileUpload(MultipartFile file, User user) throws IOException {
        BufferedOutputStream out = null;
        try {
            if (!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                logger.info("文件名:" + originalFilename);
                out = new BufferedOutputStream(
                        new FileOutputStream(
                                new File(originalFilename)
                        )
                );
                out.write(file.getBytes());
                out.flush();
                //用户id作为子目录
                String destPath = fileAddress + "/" + user.getId();
                logger.info("目的地址：" + destPath);
                //uploadPath为最终图片保存的路径
                String uploadPath = Upload.uploadFile(originalFilename, destPath);
                //查询原始用户信息
                long userId = user.getId();
                User oldUser = userRepository.findById(userId);
                oldUser.setImagePath(uploadPath);
                userRepository.save(oldUser);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
