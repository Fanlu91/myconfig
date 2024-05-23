package com.flhai.myconfig.server;

import com.flhai.myconfig.server.dal.ConfigsMapper;
import com.flhai.myconfig.server.model.Configs;
import com.flhai.myconfig.server.model.DistributedLocks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class MyConfigController {

    @Autowired
    ConfigsMapper configsMapper;

    @Autowired
    DistributedLocks distributedLocks;

    Map<String, Long> VERSIONS = new HashMap<>();

    @GetMapping("/list")
    public List<Configs> listConfig(String app, String env, String ns) {

        List<Configs> configs = configsMapper.list(app, env, ns);
//        log.info("list config: {}", configs);
        return configs;
    }

    @PostMapping("/update")
    public void updateConfig(@RequestParam String app, @RequestParam String env, @RequestParam String ns, @RequestBody Map<String, String> params) {
        params.forEach((k, v) -> {
            insertOrUpdate(new Configs(app, env, ns, k, v));
        });
        VERSIONS.put(app + "_" + env + "_" + ns, System.currentTimeMillis());
    }

    private void insertOrUpdate(Configs configs) {
        Configs config = configsMapper.select(configs.getApp(), configs.getEnv(), configs.getNs(), configs.getPkey());
        if (config == null) {
            configsMapper.insert(configs);
        } else {
            configsMapper.update(configs);
        }
    }

    @GetMapping("/version")
    public Long getVersion(String app, String env, String ns) {
        return VERSIONS.getOrDefault(app + "_" + env + "_" + ns, -1L);
    }

    @GetMapping("/locked")
    public boolean isLocked() {
        return distributedLocks.getLocked().get();
    }
}
