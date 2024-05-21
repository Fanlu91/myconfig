package com.flhai.myconfig.client.config;

import lombok.AllArgsConstructor;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;

@AllArgsConstructor
public class MyConfigServiceImpl implements MyConfigService {

    Map<String, String> config;
    ApplicationContext applicationContext;

    @Override

    public String[] getPropertyNames() {
        return this.config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return this.config.get(name);
    }

    @Override
    public void onChange(ChangeEvent event) {
        this.config = event.config();
        if (!config.isEmpty()) {
            applicationContext.publishEvent(new EnvironmentChangeEvent(config.keySet()));
        }
    }
}
