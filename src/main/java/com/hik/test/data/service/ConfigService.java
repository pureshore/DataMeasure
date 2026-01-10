package com.hik.test.data.service;

import com.hik.test.data.dto.entity.ConfigResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ConfigService {

    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * 从接口获取配置项
     */
    public ConfigResponse getConfigItems(String projectId, int page, int size) {
        try {
            // 模拟从接口获取配置数据
            return mockConfigData(projectId, page, size);

            // 实际使用时取消注释下面的代码
            // String apiUrl = "https://api.example.com/config/items?projectId=" + projectId + "&page=" + page + "&size=" + size;
            // return restTemplate.getForObject(apiUrl, ConfigResponse.class);

        } catch (Exception e) {
            ConfigResponse response = new ConfigResponse();
            response.setSuccess(false);
            response.setMessage("获取配置失败: " + e.getMessage());
            return response;
        }
    }

    /**
     * 模拟配置数据
     */
    private ConfigResponse mockConfigData(String projectId, int page, int size) {
        ConfigResponse response = new ConfigResponse();
        response.setSuccess(true);
        response.setMessage("获取配置成功");

        // 模拟所有配置项，添加布局信息
        List<ConfigResponse.ConfigItem> allItems = Arrays.asList(
                // 一行一个的配置项（重要配置）
                createConfigItem("baseUrl", "基础URL", "url", "", "https://api.example.com",
                        null, null, null, "API服务的基础地址", true, "single", 12),

                // 三个一行的配置项（连接相关）
                createConfigItem("timeout", "请求超时", "number", "30", "超时时间",
                        5, 300, null, "请求超时时间（秒）", true, "triple", 4),
                createConfigItem("retryCount", "重试次数", "number", "3", "重试次数",
                        0, 10, null, "请求失败时的重试次数", true, "triple", 4),
                createConfigItem("maxConnections", "最大连接数", "number", "10", "最大连接数",
                        1, 100, null, "同时最大连接数量", true, "triple", 4),

                // 一行一个的配置项（认证信息）
                createConfigItem("apiKey", "API密钥", "password", "", "请输入API密钥",
                        null, null, null, "用于API认证的密钥", true, "single", 12),

                // 三个一行的配置项（性能配置）
                createConfigItem("cacheSize", "缓存大小", "number", "100", "缓存大小",
                        0, 1000, null, "缓存大小（MB）", true, "triple", 4),
                createConfigItem("threadCount", "线程数量", "number", "5", "线程数量",
                        1, 50, null, "并发线程数量", true, "triple", 4),
                createConfigItem("batchSize", "批量大小", "number", "50", "批量大小",
                        1, 1000, null, "批量处理大小", true, "triple", 4),

                // 两个一行的配置项（数据库配置）
                createConfigItem("databaseUrl", "数据库URL", "url", "", "数据库地址",
                        null, null, null, "数据库连接地址", true, "double", 6),
                createConfigItem("databasePort", "数据库端口", "number", "3306", "端口号",
                        1, 65535, null, "数据库连接端口", true, "double", 6),

                // 一行一个的配置项（其他重要配置）
                createConfigItem("username", "用户名", "text", "", "请输入用户名",
                        null, null, null, "数据库用户名", true, "single", 12),
                createConfigItem("password", "密码", "password", "", "请输入密码",
                        null, null, null, "数据库密码", true, "single", 12),

                // 三个一行的配置项（日志配置）
                createConfigItem("logLevel", "日志级别", "text", "INFO", "日志级别",
                        null, null, null, "应用程序日志级别", true, "triple", 4),
                createConfigItem("logRetention", "日志保留", "number", "30", "保留天数",
                        1, 365, null, "日志文件保留天数", true, "triple", 4),
                createConfigItem("logMaxSize", "日志大小", "number", "100", "最大大小",
                        1, 1024, null, "单个日志文件最大大小（MB）", true, "triple", 4)
        );

        // 分页逻辑保持不变
        int total = allItems.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);

        List<ConfigResponse.ConfigItem> pageItems = allItems.subList(start, end);

        response.setData(pageItems);

        ConfigResponse.Pagination pagination = new ConfigResponse.Pagination();
        pagination.setPage(page);
        pagination.setSize(size);
        pagination.setTotal(total);
        pagination.setTotalPages(totalPages);
        response.setPagination(pagination);

        return response;
    }

    private ConfigResponse.ConfigItem createConfigItem(String key, String label, String type,
                                                       String value, String placeholder,
                                                       Integer min, Integer max, String pattern,
                                                       String description, boolean required,
                                                       String layout, int span) {
        ConfigResponse.ConfigItem item = new ConfigResponse.ConfigItem();
        item.setKey(key);
        item.setLabel(label);
        item.setType(type);
        item.setValue(value);
        item.setPlaceholder(placeholder);
        item.setMin(min);
        item.setMax(max);
        item.setPattern(pattern);
        item.setDescription(description);
        item.setRequired(required);
        item.setLayout(layout);
        item.setSpan(span);
        return item;
    }
}
