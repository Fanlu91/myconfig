package com.flhai.myconfig.client.repository;

import com.flhai.myconfig.client.config.ConfigMeta;

import java.util.Map;

public interface MyRepositoryChangeListener {
    void onChange(ChangeEvent event);

    record ChangeEvent(ConfigMeta configMeta, Map<String, String> config) {
    }
}
