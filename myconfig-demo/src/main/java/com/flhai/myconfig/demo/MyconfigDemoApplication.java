package com.flhai.myconfig.demo;

import com.flhai.myconfig.client.annotation.EnableMyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
@EnableMyConfig
@EnableConfigurationProperties({MyConfigProperty.class})
public class MyconfigDemoApplication {
    @Autowired
    Environment environment;

    @Autowired
    MyConfigProperty myConfigProperty;
    @Value("${my.a}")
    String a;


    public static void main(String[] args) {
        SpringApplication.run(MyconfigDemoApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner() {
        System.out.println(environment.getProperty("my.a"));
        return args -> {
            System.out.println(a);
            System.out.println(myConfigProperty.getA());
        };
    }
}
