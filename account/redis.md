#java连接 redis注意事项
## 导包
```
<!--添加redis依赖包-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!--添加第三方的验证码库-->
<dependency>
    <groupId>cn.apiclub.tool</groupId>
    <artifactId>simplecaptcha</artifactId>
</dependency>
```
## 配置
```
spring:
  redis:
    host: 172.16.31.233
    database: 0
    port: 6379
    jedis:
      pool:
        max-idle: 8
        min-idle: 0
        max-active: 8
        max-wait: -1ms
    timeout: 2000ms
```
## 添加redis配置类
RedisConfig.class
## 使用redis