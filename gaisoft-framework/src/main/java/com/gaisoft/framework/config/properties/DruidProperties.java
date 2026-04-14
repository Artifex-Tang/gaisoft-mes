package com.gaisoft.framework.config.properties;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DruidProperties {
    @Value(value="${spring.datasource.druid.initialSize}")
    private int initialSize;
    @Value(value="${spring.datasource.druid.minIdle}")
    private int minIdle;
    @Value(value="${spring.datasource.druid.maxActive}")
    private int maxActive;
    @Value(value="${spring.datasource.druid.maxWait}")
    private int maxWait;
    @Value(value="${spring.datasource.druid.connectTimeout}")
    private int connectTimeout;
    @Value(value="${spring.datasource.druid.socketTimeout}")
    private int socketTimeout;
    @Value(value="${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    @Value(value="${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    @Value(value="${spring.datasource.druid.maxEvictableIdleTimeMillis}")
    private int maxEvictableIdleTimeMillis;
    @Value(value="${spring.datasource.druid.validationQuery}")
    private String validationQuery;
    @Value(value="${spring.datasource.druid.testWhileIdle}")
    private boolean testWhileIdle;
    @Value(value="${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow;
    @Value(value="${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn;

    public DruidDataSource dataSource(DruidDataSource datasource) {
        datasource.setInitialSize(this.initialSize);
        datasource.setMaxActive(this.maxActive);
        datasource.setMinIdle(this.minIdle);
        datasource.setMaxWait((long)this.maxWait);
        datasource.setConnectTimeout(this.connectTimeout);
        datasource.setSocketTimeout(this.socketTimeout);
        datasource.setTimeBetweenEvictionRunsMillis((long)this.timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis((long)this.minEvictableIdleTimeMillis);
        datasource.setMaxEvictableIdleTimeMillis((long)this.maxEvictableIdleTimeMillis);
        datasource.setValidationQuery(this.validationQuery);
        datasource.setTestWhileIdle(this.testWhileIdle);
        datasource.setTestOnBorrow(this.testOnBorrow);
        datasource.setTestOnReturn(this.testOnReturn);
        return datasource;
    }
}
