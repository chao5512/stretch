package com.deepthoughtdata.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "hadoop")
@Data
@NoArgsConstructor
public class UploadConifg {
    private Map<String ,String> mapProps=new HashMap<String ,String>();
}
