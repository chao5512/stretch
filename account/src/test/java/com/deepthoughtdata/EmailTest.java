package com.deepthoughtdata;

import com.deepthoughtdata.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: jaysyd
 * @Date: 2018/4/27 15:54
 * @Description:
 */

@RunWith(SpringJUnit4ClassRunner. class)
@SpringBootTest
public class EmailTest {

    @Autowired
    private JavaMailSender javaMailSender;
    @Test
    public void testSend(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1046276907@qq.com");//发送者.
        message.setTo("984972185@qq.com");//接收者.
        message.setSubject("测试邮件（邮件主题）");//邮件主题.
        message.setText("这是邮件内容");//邮件内容.
        javaMailSender.send(message);//发送邮件
    }

    @Test
    public void testDate(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String nTime = DateUtils.formatDateToString(new Date(now.getTime()+300000), DateUtils.DATE_FORMAT_FULL);
        System.out.println("StringNTime=" + nTime);

        Long exp = DateUtils.formatStringToDate(nTime, DateUtils.DATE_FORMAT_FULL).getTime();

        Long nowTime = new Date().getTime();
        System.out.println(nowTime >= exp);


    }

}
