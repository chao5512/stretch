package com.deepthoughtdata.service;

import com.alibaba.fastjson.JSONObject;
import com.deepthoughtdata.util.HttpClientUtil;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class TokenService {
    @Value("${token.url}")
    private String url;

    private String charset;

    @Value("${token.client_id}")
    public String client_id;

    @Value("${token.type}")
    public String type;

    @Value("${token.scope}")
    public String scope;

    @Value("${token.client_secret}")
    public String secret;
    private final Logger logger = LoggerFactory.getLogger(TokenService.class);
    public String getToken(){
        String token = null;
        try {
            HttpClientUtil httpClientUtil = new HttpClientUtil();

            Map<String, String> createMap = new HashMap<String, String>();
            createMap.put("client_id", client_id);
            createMap.put("client_secret", secret);
            createMap.put("grant_type", type);
            String httpOrgCreateTestRtn = httpClientUtil.doPost(url, createMap, charset);

            JSONObject obj = JSONObject.parseObject(httpOrgCreateTestRtn);
             token = obj.get("access_token").toString();

        }catch (Exception e){
            logger.info("get token url:"+url);
            logger.info("get token param:client_id:"+client_id+",secret:"+secret+",type:"+type);
            e.printStackTrace();
        }
        return token;
    }
}
