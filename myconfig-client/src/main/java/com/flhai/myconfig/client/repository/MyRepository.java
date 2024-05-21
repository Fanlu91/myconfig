package com.flhai.myconfig.client.repository;

import com.flhai.myconfig.client.config.ConfigMeta;

import java.util.Map;

/**
 * interface for get config from remote server
 */
public interface MyRepository {

    static MyRepository getDefault(ConfigMeta configMeta) {
        return new MyRepositoryImpl(configMeta);
    }

    Map<String, String> getConfig();

    void addListener(MyRepositoryChangeListener listener);

}
