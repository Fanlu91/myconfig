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
}