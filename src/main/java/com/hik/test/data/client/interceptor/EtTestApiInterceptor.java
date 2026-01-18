package com.hik.test.data.client.interceptor;

import com.hik.test.data.config.EtTestApiConfig;
import com.hik.test.data.enums.EtTestApiCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EtTestApiInterceptor implements ClientHttpRequestInterceptor {

    private final EtTestApiConfig etTestApiConfig;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        // 1. 判断请求属于哪个分类
        EtTestApiCategory category = determineCategory(request.getURI().toString());

        // 2. 获取分类配置
        EtTestApiConfig.CategoryConfig categoryConfig =
                etTestApiConfig.getCategoryConfig(category.getCode());

        // 3. 添加分类特定的请求头
        request.getHeaders().set("X-API-Category", category.getCode());
        request.getHeaders().set("X-API-Version", categoryConfig.getVersion());

        // 4. 添加通用请求头
        request.getHeaders().set("User-Agent", "ET-Test-Client/1.0");

        // 5. 记录分类特定的日志
        if (log.isDebugEnabled()) {
            log.debug("ET测试API请求 - 分类[{}]: {} {}",
                    category.getDescription(), request.getMethod(), request.getURI());
        }

        // 6. 执行请求
        long startTime = System.currentTimeMillis();
        ClientHttpResponse response = execution.execute(request, body);
        long duration = System.currentTimeMillis() - startTime;

        // 7. 记录分类特定的性能指标
        if (log.isDebugEnabled()) {
            log.debug("ET测试API响应 - 分类[{}]: {} {} 耗时{}ms",
                    category.getDescription(),
                    request.getMethod(),
                    request.getURI(),
                    duration);
        }

        // 8. 监控性能（如果超过阈值记录警告）
        if (categoryConfig.getTimeout() != null && duration > categoryConfig.getTimeout()) {
            log.warn("ET测试API请求超时 - 分类[{}]: {} 耗时{}ms超过阈值{}ms",
                    category.getDescription(),
                    request.getURI(),
                    duration,
                    categoryConfig.getTimeout());
        }

        return response;
    }

    /**
     * 根据URL确定API分类
     */
    private EtTestApiCategory determineCategory(String url) {
        for (EtTestApiCategory category : EtTestApiCategory.values()) {
            if (url.contains(category.getBasePath())) {
                return category;
            }
        }
        return EtTestApiCategory.DATA_METRICS; // 默认
    }
}
