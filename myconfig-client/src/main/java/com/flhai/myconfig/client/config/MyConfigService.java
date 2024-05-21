package com.flhai.myconfig.client.config;

import com.flhai.myconfig.client.repository.MyRepository;

public interface MyConfigService {

    static MyConfigService getDefault(ConfigMeta configMeta) {
        MyRepository repository = MyRepository.getDefault(configMeta);
        return new MyConfigServiceImpl(repository.getConfig());
    }

    String[] getPropertyNames();
    String getProperty(String name);
}
