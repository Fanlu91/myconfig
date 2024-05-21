package com.flhai.myconfig.client.repository;

import cn.kimmking.utils.HttpUtils;
import com.alibaba.fastjson.TypeReference;
import com.flhai.myconfig.client.config.ConfigMeta;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class MyRepositoryImpl implements MyRepository {

    ConfigMeta meta;

    @Override
    public Map<String, String> getConfig() {
        String listPath = meta.getConfigServer() + "/list?app=" + meta.getApp()
                + "&env=" + meta.getEnv() + "&ns=" + meta.getNs();
        List<Configs> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>() {});
        Map<String, String> resultMap = new HashMap<>();
        configs.forEach(c -> resultMap.put(c.getPkey(), c.getPval()));
        return resultMap;
    }
}
