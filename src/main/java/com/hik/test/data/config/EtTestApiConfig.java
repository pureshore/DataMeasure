package com.hik.test.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "et-test")
@Data
public class EtTestApiConfig {

    /**
     * ET测试系统基础URL
     */
    private String baseUrl = "http://localhost:8080";

    /**
     * API认证token
     */
    private String apiToken;

    /**
     * 连接超时（毫秒）
     */
    private Integer connectTimeout = 3000;

    /**
     * 读取超时（毫秒）
     */
    private Integer readTimeout = 10000;

    /**
     * 是否启用API调用
     */
    private Boolean enabled = true;

    /**
     * 各分类API配置
     */
    private Map<String, CategoryConfig> categories = new HashMap<>();

    /**
     * 获取分类配置
     */
    public CategoryConfig getCategoryConfig(String category) {
        return categories.getOrDefault(category, new CategoryConfig());
    }

    /**
     * 分类配置
     */
    @Data
    public static class CategoryConfig {
        /**
         * 是否启用该分类
         */
        private Boolean enabled = true;

        /**
         * 该分类的API版本
         */
        private String version = "v1";

        /**
         * 超时时间覆盖（毫秒）
         */
        private Integer timeout;

        /**
         * 最大重试次数
         */
        private Integer maxRetries = 2;

        /**
         * 是否启用缓存
         */
        private Boolean cacheEnabled = false;

        /**
         * 缓存时间（秒）
         */
        private Integer cacheTtl = 300;
    }

    /**
     * 创建专用的RestTemplate
     */
    @Bean("etTestRestTemplate")
    public RestTemplate etTestRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        return new RestTemplate(factory);
    }
}
