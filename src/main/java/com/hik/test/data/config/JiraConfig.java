package com.hik.test.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix = "jira")
@Data
public class JiraConfig {

    /**
     * JIRA系统基础URL
     */
    private String baseUrl;

    /**
     * JIRA项目Key
     */
    private String projectKey;

    /**
     * API用户名
     */
    private String username;

    /**
     * API密码/令牌
     */
    private String password;

    /**
     * 是否启用同步
     */
    private Boolean syncEnabled = false;

    /**
     * 同步任务cron表达式
     */
    private String syncCron = "0 0/30 * * * ?";

    /**
     * 每次同步最大数量
     */
    private Integer maxSyncCount = 100;

    /**
     * 连接超时时间（毫秒）
     */
    private Integer connectTimeout = 5000;

    /**
     * 读取超时时间（毫秒）
     */
    private Integer readTimeout = 30000;

    @Bean
    public RestTemplate jiraRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        return new RestTemplate(factory);
    }
}