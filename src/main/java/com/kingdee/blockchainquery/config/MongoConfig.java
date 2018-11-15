package com.kingdee.blockchainquery.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2018/3/14.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mongofabric.data")
public class MongoConfig {

    private String block;
    private String transaction;
    private String peer;
}
