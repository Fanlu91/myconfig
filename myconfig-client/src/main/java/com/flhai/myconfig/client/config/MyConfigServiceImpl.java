package com.flhai.myconfig.client.config;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class MyConfigServiceImpl implements MyConfigService {

    Map<String, String> config;

    @Override

    public String[] getPropertyNames() {
        return this.config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return this.config.get(name);
    }
}
