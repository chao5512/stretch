package com.deepthoughtdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.sidecar.EnableSidecar;

/**
 * PipeApplication
 *
 */
@EnableSidecar
@SpringBootApplication
public class PipeApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(PipeApplication.class, args);
    }
}
