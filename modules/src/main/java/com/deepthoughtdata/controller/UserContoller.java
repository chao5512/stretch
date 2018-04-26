package com.deepthoughtdata.controller;

import com.deepthoughtdata.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("user")
public class UserContoller {
    private final Logger logger = LoggerFactory.getLogger(UserContoller.class);

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/toLogin")
    public String toLogin(){

        return "login";
    }

    @RequestMapping(value= "/login")
    public String login(HttpServletRequest request, HttpServletResponse response){
        logger.info("check token and logging");
        Cookie cookie = new Cookie("token", tokenService.getToken());
        cookie.setPath("/");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.addCookie(cookie);
        System.out.println(cookie.getValue());
        return "";
    }

}
