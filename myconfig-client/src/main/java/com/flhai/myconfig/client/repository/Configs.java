package com.flhai.myconfig.client.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Configs {
    private String app;
    private String env;
    private String ns;
    private String pkey;
    private String pval;
}
