package com.flhai.myconfig.client.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
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
        Set<String> keys = getChangedKeys(event.config());

        if (keys.isEmpty()) {
            log.info("[MYCONFIG] EnvironmentChangeEvent published but no config changed, ignored.");
            return;
        }
        this.config = event.config();
        log.info("[MYCONFIG] EnvironmentChangeEvent published with new config keys: " + keys);
        applicationContext.publishEvent(new EnvironmentChangeEvent(keys));

    }

    private Set<String> getChangedKeys(Map<String, String> config) {
        if (config == null) {
            return this.config.keySet();
        }
        if (this.config == null) {
            return config.keySet();
        }

        // keys new config has that doesn't equal to old config
        Set<String> diff = config.keySet().stream().filter(
                k -> !config.get(k).equals(this.config.get(k))).collect(Collectors.toSet());
        // keys old config has that new config doesn't have
        this.config.keySet().stream().filter(k -> !config.containsKey(k)).forEach(diff::add);
        return diff;
    }
}
