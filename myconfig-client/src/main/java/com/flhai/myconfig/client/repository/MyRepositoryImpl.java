package com.flhai.myconfig.client.repository;

import cn.kimmking.utils.HttpUtils;
import com.alibaba.fastjson.TypeReference;
import com.flhai.myconfig.client.config.ConfigMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MyRepositoryImpl implements MyRepository {

    ConfigMeta meta;

    Map<String, Long> versionMap = new HashMap<>();
    Map<String, Map<String, String>> configMap = new HashMap<>();
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    List<MyRepositoryChangeListener> listeners;

    public MyRepositoryImpl(ConfigMeta meta) {
        this.meta = meta;
        this.listeners = new ArrayList<>();
        executorService.scheduleAtFixedRate(this::heartbeat, 1, 5, java.util.concurrent.TimeUnit.SECONDS);
    }

    private void heartbeat() {
        String versionPath = meta.versionPath();
        long version = HttpUtils.httpGet(versionPath, new TypeReference<Long>() {
        });
        long oldVersion = versionMap.getOrDefault(meta.genKey(), -1L);
        if (version > oldVersion) {
            System.out.println("config changed, old version: " + oldVersion + ", new version: " + version);
            versionMap.put(meta.genKey(), version);
            Map<String, String> newConfigs = listConfigs();
            configMap.put(meta.genKey(), newConfigs);
            System.out.println("publish event with keys: " + newConfigs.keySet());
            // publish event in order to refresh the environment
            listeners.forEach(l -> l.onChange(new MyRepositoryChangeListener.ChangeEvent(meta, newConfigs)));
        }
    }

    public void addListener(MyRepositoryChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public Map<String, String> getConfig() {
        if (configMap.containsKey(meta.genKey())) {
            return configMap.get(meta.genKey());
        }
        return listConfigs();
    }

    @NotNull
    private Map<String, String> listConfigs() {
        List<Configs> configs = HttpUtils.httpGet(meta.listPath(), new TypeReference<List<Configs>>() {
        });
        Map<String, String> resultMap = new HashMap<>();
        configs.forEach(c -> resultMap.put(c.getPkey(), c.getPval()));
        return resultMap;
    }

}
