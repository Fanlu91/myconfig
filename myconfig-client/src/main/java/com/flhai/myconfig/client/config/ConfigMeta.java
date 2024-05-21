package com.flhai.myconfig.client.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConfigMeta {
    String app;
    String env;
    String ns;
    String configServer;

    public String genKey() {
        return getApp() + "_" + getEnv() + "_" + getNs();
    }

    public String listPath() {
        return configServer + "/list?app=" + app + "&env=" + env + "&ns=" + ns;
    }

    public String versionPath() {
        return configServer + "/version?app=" + app + "&env=" + env + "&ns=" + ns;
    }


}