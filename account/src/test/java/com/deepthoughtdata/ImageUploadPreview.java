package com.deepthoughtdata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;

/**
 * @ClassName ImageUploadPreview
 * @Description TODO
 * @Auther: 王培文
 * @Date: 2018/5/16 
 * @Version 1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageUploadPreview {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    @Test
    public void upload() throws Exception {
        FileInputStream fis = new FileInputStream("C:\\Users\\hadoop\\Desktop\\aa.jpg");
        MockMultipartFile mmfile = new MockMultipartFile("file", "aa.jpg", "image/jpg", fis);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.fileUpload("/user/upload")
                        .file(mmfile)
                        .param("userid", "2")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

}
