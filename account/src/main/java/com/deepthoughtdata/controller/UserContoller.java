package com.deepthoughtdata.controller;

import com.deepthoughtdata.entity.User;
import com.deepthoughtdata.service.TokenService;
import com.deepthoughtdata.service.UserService;
import com.deepthoughtdata.util.DateUtils;
import com.deepthoughtdata.util.MD5Util;
import com.deepthoughtdata.util.ResultUtil;
import com.deepthoughtdata.util.Upload;
import com.deepthoughtdata.util.ValidateCode;
import com.deepthoughtdata.vo.Result;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.UTF8;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("user")
@Api(value = "用户管理", description = "用户管理模块")
public class UserContoller {
    private final Logger logger = LoggerFactory.getLogger(UserContoller.class);

    @Value("${server.ip}:${server.port}")
    private String mailUri;

    @Value("${spring.mail.sTime}")
    private Long sTime;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //用户登录接口
    @ApiOperation(value = "用户登录",httpMethod = "POST")
    @RequestMapping(value= "/login",method = RequestMethod.POST)
    @ResponseBody
    public Result login(@RequestParam(name="email",required = true) String email,
            @RequestParam(name="password",required = true) String password, HttpServletResponse response){
        User user = null;
        try {
            user = userService.findByEmailAndPassword(email, password);
            Result result = null;
            if(user == null){
                result = ResultUtil.error(-1, "账户名或密码错误！");
                return result;
            }else if(user.getStatus() == 0){
                result = ResultUtil.error(-1, "账号未激活，请先激活账号！");
                return result;
            }
            logger.info("check token and logining");
            Map<String, Object> map = new HashMap<>();
            map.put("token", tokenService.getToken());
            map.put("id",user.getId());
            map.put("username",user.getUsername());
            map.put("email",user.getEmail());
            map.put("gender",user.getGender());
            map.put("region",user.getRegion());
            map.put("birthday",user.getBirthday());
            map.put("career",user.getCareer());
            map.put("status",user.getStatus());
            map.put("imagePath",user.getImagePath());
            result = ResultUtil.success(map);
            System.out.println("data=" + result.getData());
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("错误！",e);
            return ResultUtil.error(-1, "未知错误！");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("错误！",e);
            return ResultUtil.error(-1, "未知错误！");
        }

    }

    //判断邮箱是否存在
    @ApiOperation(value = "邮箱是否已可以注册",httpMethod = "POST")
    @RequestMapping(value = "exist",method = RequestMethod.POST)
    @ResponseBody
    public Result checkAccount(@RequestParam(name="email",required = true) String email) throws Exception{
        Result result = null;
        if(userService.findByEmailAndStatus(email, 1L) != null){
            logger.info("该邮箱已被注册！");
            return ResultUtil.success(false);
        }
        return ResultUtil.success(true);
    }


    //注册功能
    @ApiOperation(value = "注册",httpMethod = "POST")
    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Result register(User user) throws Exception{
        String email = user.getEmail();
        if(userService.findByEmailAndStatus(email, 1L) != null){
            logger.info("注册失败，该邮箱已被注册！");
            return ResultUtil.error(-1, "该邮箱已被注册！");
        }
        String mailActiveCode = "";
        mailActiveCode = UUID.randomUUID().toString();
        user.setStatus(0L);     //未激活状态
        user.setToken(mailActiveCode); //设置验证码
        Date now = new Date();
        user.setRegtime(DateUtils.formatDateToString(now, DateUtils.DATE_FORMAT_FULL));
        //设置验证码过期时间
        user.setToken_exptime(DateUtils.formatDateToString(
                new Date(now.getTime()+sTime), DateUtils.DATE_FORMAT_FULL));
        String link = "http://" + mailUri + "/user/registered?mailcode=" + mailActiveCode;
        userService.userValidate(user, link);
        userService.save(user);
        return ResultUtil.success();
    }

    //激活完成注册
    @ApiOperation(value = "注册激活",httpMethod = "GET")
    @RequestMapping(value = "registered",method = RequestMethod.GET)
    @Transactional
    @ResponseBody
    public Result registered(HttpServletRequest request, Model model){
        String code = "";
        code = request.getParameter("mailcode");
        User user = userService.findByToken(code);
        if(user == null){
            return ResultUtil.error(-1, "用户不存在！");

        }else if(new Date().getTime() > DateUtils.formatStringToDate(
                user.getToken_exptime(), DateUtils.DATE_FORMAT_FULL).getTime()){
            return ResultUtil.error(-1, "激活时间已过期！");
        }
        userService.modifyByToken(code, 1L);
        return ResultUtil.success();

    }

    //邮箱找回发送邮件功能
    @ApiOperation(value = "找回密码",httpMethod = "POST")
    @RequestMapping(value = "getBack",method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public Result getBack(User user,@RequestParam("code")String code,@RequestParam("uuidKey") String uuid,HttpServletRequest request){
        logger.info("code值：" + code);
        //验证码校验
        if (uuid != null) {
            //获取redis中存储的验证码
            String redisCode = redisTemplate.opsForValue().get(uuid);
            logger.info("redisCode：" + redisCode);
            if (redisCode==null) {
                logger.error("验证码超时|不存在");
                return ResultUtil.error(-5,"验证码超时|不存在");
            }else{
                if (code !=null){
                    if (!code.equalsIgnoreCase(redisCode)){
                        logger.error("验证码比对失败");
                        return ResultUtil.error(-4,"验证码比对失败");
                    }
                }else{
                    return ResultUtil.error(-1,"code未取到值");
                }
            }
        }else{
            logger.error("未取得相应uuid的key信息");
            return ResultUtil.error(-1,"未取得相应uuid的key信息");
        }
        logger.info("比对成功");
        //比对成功之后进行接下来操作
        User user1 = userService.findByEmailAndStatus(user.getEmail(), 1L);
        if(user1 == null){
            return ResultUtil.error(-2, "该用户不存在！");
        }
        userService.userValidate(user1,"http://localhost:8088/templates/getBack.html?email="+ user1.getEmail());
        return ResultUtil.success();
    }

    //重置密码功能
    @ApiOperation(value = "重置密码",httpMethod = "POST")
    @RequestMapping(value = "rpasswd",method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public Result rpasswd(User user) throws Exception{
        User user1 = userService.findByEmailAndStatus(user.getEmail(), 1L);
        if(user1 == null){
            logger.error("用户不存在！");
            return ResultUtil.error(-1, "该用户不存在！");
        }
//      使用MD5+salt对用户密码进行加密
        String salt = MD5Util.createSalt();
        String password = MD5Util.encode(user.getPassword(), salt);
        System.out.println("password="+password);
        userService.modifyByEmailAndPasswordAndSalt(user.getEmail(), password, salt);
        return ResultUtil.success();
    }

    //修改密码功能
    @ApiOperation(value = "修改密码",httpMethod = "POST")
    @RequestMapping(value = "updatePasswd",method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public Result updatePasswd(User user) throws Exception{
        User user1 = userService.findByEmailAndStatus(user.getEmail(), 1L);
        if(user1 == null){
            logger.error("用户不存在！");
            return ResultUtil.error(-1, "该用户不存在！");
        }
//      使用MD5+salt对用户密码进行加密
        String salt = MD5Util.createSalt();
        String password = MD5Util.encode(user.getPassword(), salt);
        System.out.println("password="+password);
        userService.modifyByEmailAndPasswordAndSalt(user.getEmail(), password, salt);
        return ResultUtil.success();
    }

    //修改账户信息
    @ApiOperation(value = "更新账户信息",httpMethod = "POST")
    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Result updateUserInfo(User user) throws Exception{
        if(userService.findByUserId(user.getId()) == null){
            return ResultUtil.error(-1, "用户不存在！");
        }
        userService.modifyUserInfo(user);
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
    @ApiOperation(value = "上传图片",httpMethod = "POST")
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
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
    @ApiOperation(value = "加载图片",httpMethod = "POST")
    @RequestMapping(value = "/ioReadImage/{userid}",method = RequestMethod.POST)
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
     * 功能描述:验证码生成接口
     * @param response
     * @return: byte[]
     * @auther: 王培文
     * @date: 2018/5/17 17:05
     */
    @ApiOperation(value = "验证码生成",httpMethod = "POST")
    @RequestMapping(value = "/validateCode", method = RequestMethod.POST)
    @ResponseBody
    public byte[] code(HttpServletResponse response){
        //通过验证码生成工具，生成验证码
        ValidateCode validateCode = new ValidateCode(100, 30, 5, 10);
        String code = validateCode.getCode();
        //将验证码存放到redis中
        String uuid = UUID.randomUUID().toString();
        logger.info("uuid：" + uuid);
        logger.info("验证码："+ code);
        //uuid作为key，code作为value，保存2*60秒
        redisTemplate.opsForValue().set(uuid, code, 2*60, TimeUnit.SECONDS);
        //将验证码的key，及验证码图片返回。
        response.setHeader("validateCode",uuid);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            validateCode.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
