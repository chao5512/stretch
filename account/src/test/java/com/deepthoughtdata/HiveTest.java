package com.deepthoughtdata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HiveTest {
    /*java 操作hive依赖jar包，注意使用的jar包最好与hadoop和hive的版本相对应，否则可能会报错
    hadoop-common-2.6.0.jar
    hive-jdbc-1.0.0.jar
     */
    private static String driverName ="org.apache.hive.jdbc.HiveDriver";
    private static String Url="jdbc:hive2://172.16.31.91:10000/default";
    @Test
    public void test(){

    }
}
