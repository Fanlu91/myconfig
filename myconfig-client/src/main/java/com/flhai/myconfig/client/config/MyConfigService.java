package com.flhai.myconfig.client.config;

import com.flhai.myconfig.client.repository.MyRepository;
import com.flhai.myconfig.client.repository.MyRepositoryChangeListener;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public interface MyConfigService extends MyRepositoryChangeListener {

    static MyConfigService getDefault(ApplicationContext applicationContext, ConfigMeta configMeta) {
        MyRepository repository = MyRepository.getDefault(configMeta);
        Map<String, String> newConfigs = repository.getConfig();
        MyConfigService myConfigService = new MyConfigServiceImpl(newConfigs, applicationContext);
        repository.addListener(myConfigService);
        return myConfigService;
    }


    String[] getPropertyNames();

    String getProperty(String name);
}
