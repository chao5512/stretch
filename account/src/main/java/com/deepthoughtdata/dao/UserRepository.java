package com.deepthoughtdata.dao;

import com.deepthoughtdata.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @Author: jaysyd
 * @Date: 2018/4/27 16:31
 * @Description: 用户管理dao层
 */
public interface  UserRepository extends JpaRepository<User, Long> {

    User findById(long id); //根据id查找用户

    void deleteById(Long id); //根据id删除用户

    User findByEmailAndStatus(String email, Long status); //根据email查找用户信息

    User findByUsername(String username); //根据username查找用户信息

    User save(User user); //增加用户

    User findAllByToken(String code); //根据激活码查找用户

    @Modifying
    @Query("update User u set u.status = ?2 where u.token = ?1")
    int modifyByTokenAndStatus(String  token, Long status); //修改用户激活状态

    User findByEmailAndPassword(String email, String password); //查找用户是否匹配

    @Modifying
    @Query("update User u set u.password = ?2 where u.email = ?1")//修改用户密码
    int modifyByEmailAndPassword(String email, String password);

}
