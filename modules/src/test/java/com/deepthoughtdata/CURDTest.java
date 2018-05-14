package com.deepthoughtdata;

import com.deepthoughtdata.dao.UserRepository;
import com.deepthoughtdata.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CURDTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    public void save(){
    }
}
