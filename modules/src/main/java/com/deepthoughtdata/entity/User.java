package com.deepthoughtdata.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String username;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private String email;
  @Column(nullable = false)
  private String token;
  @Column(nullable = false)
  private Long token_exptime;
  @Column(nullable = false)
  private Long status;

  @Column(nullable = false)
  private Long regtime;

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
