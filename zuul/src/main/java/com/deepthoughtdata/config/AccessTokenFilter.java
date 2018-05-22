package com.deepthoughtdata.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.deepthoughtdata.util.ResultUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * @Author: jaysyd
 * @Date: 2018/5/20 17:10
 * @Description:
 */
public class AccessTokenFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre"; //前置过滤器
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        System.out.println(String.format("%s AccessUserNameFilter request to %s",
                request.getMethod(), request.getRequestURL().toString()));
        String ignore = request.getRequestURL().substring(request.getRequestURL().lastIndexOf("/")+1,request.getRequestURL().length());
        System.out.println(ignore);
        String token = request.getHeader("access-token");
        System.out.println("token = " + token);
        if(token != null && !"".equals(token)){
//            response.addHeader("Access-Control-Allow-Origin", "*");
            ctx.setSendZuulResponse(true);
            ctx.setResponseStatusCode(200);
            ctx.set("isSuccess", true);

        }else if("login".equals(ignore) || "exist".equals(ignore) ||
                "register".equals(ignore) || "registered".equals(ignore) ||
                "getBack".equals(ignore) || "rpasswd".equals(ignore) ||
                "validateCode".equals(ignore)){
            ctx.setSendZuulResponse(true);
            ctx.setResponseStatusCode(200);
            ctx.set("isSuccess", true);
        }else{
            //response.addHeader("Access-Control-Allow-Origin", "*");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(200);
            ctx.setResponseBody("{\"code\":\"1\",\"msg\":\"token无效！\"}");
            ctx.getResponse().setContentType("text/html;charset=UTF-8");
            ctx.set("isSuccess", false);
        }
        return null;

    }
}
