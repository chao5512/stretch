package com.deepthoughtdata;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author: jaysyd
 * @Date: 2018/5/9 15:17
 * @Description: HDFS文件上传下载测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HDFSTest {
    FileSystem fs = null;
    @Before
    public void init(){
        try {
            //初始化文件系统
            fs = FileSystem.get(new URI("hdfs://172.16.31.232:9000"), new Configuration(), "hadoop");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {

    }
    @Test
    /**
     * HDFS文件上传
     */
    public void testFileUpload(){
        try {
            OutputStream os = fs.create(new Path("/user/hadoop/女仆3.jpg"));
            FileInputStream fis = new FileInputStream("E://beh/tmp/女仆2.jpg");
            IOUtils.copyBytes(fis, os, 2048,true);
            //可以使用hadoop提供的简单方式
//            fs.copyFromLocalFile(new Path("I://test.log"), new Path("/test.log"));
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    /**
     * HDFS文件下载
     */
    public void testFileDownload(){
        try {
            InputStream is = fs.open(new Path("/user/hadoop/女仆1.jpg"));
            FileOutputStream fos = new FileOutputStream("E://beh/tmp/女仆2.jpg");
            IOUtils.copyBytes(is, fos, 2048);
            //可以使用hadoop提供的简单方式
//            fs.copyToLocalFile(new Path("/test.log"), new Path("E://test.log"));
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

}
