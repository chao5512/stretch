package com.deepthoughtdata.controller;

import com.deepthoughtdata.entity.User;
import com.deepthoughtdata.service.TokenService;
import com.deepthoughtdata.service.UserService;
import com.deepthoughtdata.util.ResultUtil;
import com.deepthoughtdata.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("user")
public class UserContoller {
    private final Logger logger = LoggerFactory.getLogger(UserContoller.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/toLogin")
    public String toLogin(){

        return "login";
    }

    @RequestMapping(value= "/login")
    @ResponseBody
    public Result login(@RequestParam("username") String username,
            @RequestParam("password") String password, HttpServletResponse response){

        User user = userService.findByUsernameAndPassword(username, password);
        Result result = null;
        if(user == null){
            System.out.println(username+"....................+++++++++++++"+password);
            result = ResultUtil.error(-1, "username or password is not exists!");
            return result;
        }
        logger.info("check token and logging");
        Cookie cookie = new Cookie("token", tokenService.getToken());
        cookie.setPath("/");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.addCookie(cookie);
        System.out.println(cookie.getValue());
        result = ResultUtil.success();
        return result;
    }
    @RequestMapping(value = "toRegister")
    public String toRegister() throws Exception{
        return "register";
    }

    //注册功能
    @RequestMapping(value = "register")
    @ResponseBody
    @Transactional
    public Result register(User user) throws Exception{
        Map<String, Object> msg = new HashMap<>();
        String email = user.getEmail();
        String username = user.getUsername();
        if(userService.findByEmail(email) != null){
            logger.info("注册失败，该邮箱已被注册！");
            return ResultUtil.error(-1, "该邮箱已被注册！");
        }else if(userService.findByUsername(username) != null){
            logger.info("注册失败，用户名已存在！");
            return ResultUtil.error(-1, "用户名已存在！");
        }
        String mailActiveCode = "";
        mailActiveCode = UUID.randomUUID().toString();
        user.setStatus(0L);     //未激活状态
        user.setToken(mailActiveCode);
        userService.save(user);
        userService.userValidate(user, mailActiveCode);
        return ResultUtil.success();
    }
    //完成注册
    @RequestMapping(value = "registered")
    @Transactional
    public String registered(HttpServletRequest request){
        String code = "";
        code = request.getParameter("mailcode");
        User user = userService.findByToken(code);
        if(user != null){
            userService.modifyByToken(code, 1L);
            return "index";
        }

        return null;
    }

    /**
     * 功能描述:返回upload上传界面，测试时使用，开发时可删除
     *
     * @param: []
     * @return: org.springframework.web.servlet.ModelAndView
     * @auther: 王培文
     * @date: 2018/5/10 14:15
     */
    @RequestMapping("uploadPage")
    public ModelAndView uploadPage(){
        return new ModelAndView("upload");
    }

    /**
     * 功能描述: 完成文件上传功能
     *
     * @param: [file]
     * @return: java.lang.String
     * @auther: 王培文
     * @date: 2018/5/10 14:55
     */
    @PostMapping("/upload")
    @ResponseBody
    public Result handleFileUpload(@RequestParam("file")MultipartFile file, @RequestParam("userid") String userId) {
        //图片的类型在前端界面进行判断
        try {
        long id = Long.parseLong(userId);
        //封装user对象，传给fileUpload
        User user = new User();
        user.setId(id);
        userService.fileUpload(file, user);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error(-1, "上传图像失败");
        }
        return ResultUtil.success();
    }


}
