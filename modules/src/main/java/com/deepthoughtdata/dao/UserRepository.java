package com.deepthoughtdata.dao;

import com.deepthoughtdata.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @Author: jaysyd
 * @Date: 2018/4/27 16:31
 * @Description:
 */
public interface  UserRepository extends JpaRepository<User, Long> {

    User findById(long id); //根据id查找用户

    void deleteById(Long id); //根据id删除用户

    User findByEmail(String email); //根据email查找用户信息

    User findByUsername(String username); //根据username查找用户信息

    User save(User user); //增加用户

    User findAllByToken(String code);

    @Modifying
    @Query("update User u set u.status = ?1 where u.token = ?2")
    int modifyByTokenAndStatus(String  token, Long status);

    User findByUsernameAndPassword(String username, String password);
}
