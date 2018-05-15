package com.deepthoughtdata.controller;

import com.deepthoughtdata.entity.User;
import com.deepthoughtdata.service.TokenService;
import com.deepthoughtdata.service.UserService;
import com.deepthoughtdata.util.ResultUtil;
import com.deepthoughtdata.util.Upload;
import com.deepthoughtdata.vo.Result;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("user")
public class UserContoller {
    private final Logger logger = LoggerFactory.getLogger(UserContoller.class);

    @Value("${server.ip}:${server.port}")
    private String mailUri;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/toLogin")
    public String toLogin(){
        System.out.println(mailUri);
        return "page_user_login_1";
    }

    //用户登录接口
    @RequestMapping(value= "/login")
    @ResponseBody
    public Result login(@RequestParam("email") String email,
            @RequestParam("password") String password, HttpServletResponse response){

        User user = userService.findByEmailAndPassword(email, password);
        Result result = null;
        if(user == null){
            result = ResultUtil.error(-1, "账户名或密码错误！");
            return result;
        }else if(user.getStatus() == 0){
            result = ResultUtil.error(-1, "账号未激活，请先激活账号！");
            return result;
        }

        logger.info("check token and logining");
        Cookie cookie = new Cookie("token", tokenService.getToken());
        cookie.setPath("/");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.addCookie(cookie);
        System.out.println(cookie.getValue());
        result = ResultUtil.success();
        return result;
    }

    //判断邮箱是否存在
    @RequestMapping(value = "exist")
    @ResponseBody
    public Boolean toRegister(User user) throws Exception{
        String email = user.getEmail();
        if(userService.findByEmail(email) != null){
            logger.info("该邮箱已被注册！");
            return false;
        }
        return true;
    }

    //首页
    @RequestMapping(value = "index")
    public String index(){
        return "index";
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
        String link = "http://" + mailUri + "/user/registered?mailcode=" + mailActiveCode;
        userService.userValidate(user, link);
        return ResultUtil.success();
    }
    //激活完成注册
    @RequestMapping(value = "registered")
    @Transactional
    public String registered(HttpServletRequest request){
        String code = "";
        code = request.getParameter("mailcode");
        User user = userService.findByToken(code);
        if(user != null){
            userService.modifyByToken(code, 1L);
            return "redirect:index";
        }

        return null;
    }

    //邮箱找回发送邮件功能
    @RequestMapping(value = "getBack")
    @Transactional
    public Result getBack(User user){
        User user1 = userService.findByEmail(user.getEmail());
        if(user1 == null){
            return ResultUtil.error(-1, "该用户不存在！");
        }
        userService.userValidate(user1,"http://www.baidu.com");
        return ResultUtil.success();
    }

    //重置密码功能
    @RequestMapping(value = "rpasswd")
    @Transactional
    public Result rpasswd(User user){
        User user1 = userService.findByEmail(user.getEmail());
        if(user1 == null){
            logger.error("用户不存在！");
            return ResultUtil.error(-1, "该用户不存在！");
        }
        userService.modifyByEmailAndPassword(user.getEmail(), user.getPassword());
        return ResultUtil.success();
    }

    /**
     * 功能描述:图片上传
     * @param file
     * @param userId
     * @return: com.deepthoughtdata.vo.Result
     * @auther: 王培文
     * @date: 2018/5/13 16:19
     */
    @RequestMapping("/upload")
    @ResponseBody
    public Result uploadImage(@RequestParam("file")MultipartFile file,@RequestParam("userid") String userId){
        try {
            User user = new User();
            long id = Long.parseLong(userId);
            user.setId(id);
            userService.fileUpload(file,user);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error(-1,"图片上传失败");
        }
        return ResultUtil.success();
    }

    /**
     * 功能描述:用户输入的用户id返回图片流
     * @param request
     * @param response
     * @param userid
     * @return: String
     * @auther: 王培文
     * @date: 2018/5/15 10:16
     */
    @RequestMapping(value = "/ioReadImage/{userid}",method = RequestMethod.GET)
    public String IoReadImage(HttpServletRequest request,
                              HttpServletResponse response,
                              @PathVariable String userid){
        FSDataInputStream inputStream = null;
        ServletOutputStream out = null;
        try {
            //获取用户头像保存路径
            long id = Long.parseLong(userid);
            User user = userService.findByUserId(id);
            //获取hadoop的文件系统
            FileSystem fs = Upload.getFileSystem();
            //获取输入流
            inputStream = fs.open(new Path(user.getImagePath()));
            //设置响应类型
            response.setContentType("multipart/form-data");
            out = response.getOutputStream();
            //获取文件流
            int len=0;
            byte[] buffer = new byte[1024 * 10];
            while((len = inputStream.read(buffer))!=-1){
                out.write(buffer,0 ,len);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * 功能描述:跳转到img.html页面，仅供测试使用
     * @param
     * @return: java.lang.String
     * @auther: 王培文
     * @date: 2018/5/15 10:51
     */
    @RequestMapping(value = "img")
    public String image(){
        return "image";
    }

}
