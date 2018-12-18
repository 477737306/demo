package com.example.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping(value = "test")
public class TestApplication {

    @RequestMapping(value = "/hi")
    public String test(){

        return "study docker made me happy!";
    }
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
