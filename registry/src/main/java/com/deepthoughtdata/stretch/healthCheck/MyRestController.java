package com.deepthoughtdata.stretch.healthCheck;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {

    public static boolean isDB = true;

    @GetMapping(value = "/setIsDB/{isDB}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void setIsDB(@PathVariable boolean isDB) {
        this.isDB = isDB;
    }

}
