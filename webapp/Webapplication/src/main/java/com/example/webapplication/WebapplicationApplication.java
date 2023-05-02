package com.example.webapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;



@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class WebapplicationApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebapplicationApplication.class, args);
    }


}
