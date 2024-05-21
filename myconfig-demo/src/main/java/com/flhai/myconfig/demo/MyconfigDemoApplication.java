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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@SpringBootApplication
@EnableMyConfig
@EnableConfigurationProperties({MyConfigProperty.class})
@RestController
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

    @GetMapping("/a")
    public String a() {
        return "value a: " + a + "\n"
                + "myConfigProperty: " + myConfigProperty.getA() + "\n";
    }

}
