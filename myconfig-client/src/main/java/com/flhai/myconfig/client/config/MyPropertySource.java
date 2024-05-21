package com.flhai.myconfig.client.config;

import org.springframework.core.env.EnumerablePropertySource;

/**
 * 通过实现EnumerablePropertySource接口，实现一个PropertySource
 * 类似代理 source 中提供的获取配置能力
 * 这里的source是MyConfigService
 */
public class MyPropertySource extends EnumerablePropertySource<MyConfigService> {

    public MyPropertySource(String name, MyConfigService source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperty(name);
    }
}
