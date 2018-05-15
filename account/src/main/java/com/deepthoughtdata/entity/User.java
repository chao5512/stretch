package com.deepthoughtdata.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;//id唯一标识
  @Column(nullable = false)
  private String username;//用户名
  @Column(nullable = false)
  private String password;//密码
  @Column(nullable = false)
  private String email;//邮箱
  @Column(nullable = false)
  private String token;//验证码
  @Column(nullable = false)
  private Long token_exptime;//验证码有效期
  @Column(nullable = false)
  private Long status;//激活状态
  @Column(nullable = false)
  private Long regtime;//注册时间
  @Column(nullable = false)
  //用于加密的盐值
  private String salt;

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  //用户头像路径
  @Column(nullable = true)
  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  @Column(nullable = true)
  private String imagePath;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getToken_exptime() {
    return token_exptime;
  }

  public void setToken_exptime(Long token_exptime) {
    this.token_exptime = token_exptime;
  }

  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

  public Long getRegtime() {
    return regtime;
  }

  public void setRegtime(Long regtime) {
    this.regtime = regtime;
  }
}
