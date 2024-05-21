package com.flhai.myconfig.demo;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "my")
public class MyConfigProperty {
    String a;
}
