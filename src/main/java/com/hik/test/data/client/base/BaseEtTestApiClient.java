package com.hik.test.data.client.base;

import com.hik.test.data.client.exceptions.EtTestApiException;
import com.hik.test.data.config.EtTestApiConfig;
import com.hik.test.data.enums.EtTestApiCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

/**
 * ET测试API基础客户端
 */
@Slf4j
@Component
@RequiredArgsConstructor
public abstract class BaseEtTestApiClient {

    protected final RestTemplate etTestRestTemplate;
    protected final EtTestApiConfig etTestApiConfig;

    /**
     * 获取API分类
     */
    protected abstract EtTestApiCategory getApiCategory();

    /**
     * 构建完整的API URL
     */
    protected String buildApiUrl(String endpoint) {
        EtTestApiCategory category = getApiCategory();
        return UriComponentsBuilder.fromHttpUrl(etTestApiConfig.getBaseUrl())
                .path(category.getBasePath())
                .path(endpoint)
                .build()
                .toUriString();
    }

    /**
     * 构建请求头
     */
    protected HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // 添加认证Token
        if (etTestApiConfig.getApiToken() != null && !etTestApiConfig.getApiToken().isEmpty()) {
            headers.set("Authorization", etTestApiConfig.getApiToken());
        }

        // 添加请求ID用于追踪
        headers.set("X-Request-ID", java.util.UUID.randomUUID().toString());
        headers.set("X-Client-Name", "et-test-client");

        return headers;
    }

    /**
     * 构建请求实体
     */
    protected <T> HttpEntity<T> buildRequestEntity(T body) {
        return new HttpEntity<>(body, buildHeaders());
    }

    protected HttpEntity<Void> buildRequestEntity() {
        return new HttpEntity<>(buildHeaders());
    }

    /**
     * 发送GET请求
     */
    protected <T> T get(String endpoint, Class<T> responseType, Map<String, ?> uriVariables) {
        try {
            String url = buildApiUrl(endpoint);
            log.debug("发送GET请求到ET测试API: {}", url);

            ResponseEntity<T> response = etTestRestTemplate.exchange(
                    url, HttpMethod.GET, buildRequestEntity(), responseType, uriVariables);

            checkResponse(response);
            return response.getBody();

        } catch (Exception e) {
            log.error("ET测试API GET请求失败: {}", endpoint, e);
            throw new EtTestApiException("GET请求失败: " + endpoint, e);
        }
    }

    /**
     * 发送POST请求
     */
    protected <T, R> R post(String endpoint, T requestBody, Class<R> responseType) {
        try {
            String url = buildApiUrl(endpoint);
            log.debug("发送POST请求到ET测试API: {}", url);

            ResponseEntity<R> response = etTestRestTemplate.exchange(
                    url, HttpMethod.POST, buildRequestEntity(requestBody), responseType);

            checkResponse(response);
            return response.getBody();

        } catch (Exception e) {
            log.error("ET测试API POST请求失败: {}", endpoint, e);
            throw new EtTestApiException("POST请求失败: " + endpoint, e);
        }
    }

    /**
     * 发送PUT请求
     */
    protected <T, R> R put(String endpoint, T requestBody, Class<R> responseType) {
        try {
            String url = buildApiUrl(endpoint);
            log.debug("发送PUT请求到ET测试API: {}", url);

            ResponseEntity<R> response = etTestRestTemplate.exchange(
                    url, HttpMethod.PUT, buildRequestEntity(requestBody), responseType);

            checkResponse(response);
            return response.getBody();

        } catch (Exception e) {
            log.error("ET测试API PUT请求失败: {}", endpoint, e);
            throw new EtTestApiException("PUT请求失败: " + endpoint, e);
        }
    }

    /**
     * 发送DELETE请求
     */
    protected void delete(String endpoint) {
        try {
            String url = buildApiUrl(endpoint);
            log.debug("发送DELETE请求到ET测试API: {}", url);

            ResponseEntity<Void> response = etTestRestTemplate.exchange(
                    url, HttpMethod.DELETE, buildRequestEntity(), Void.class);

            checkResponse(response);

        } catch (Exception e) {
            log.error("ET测试API DELETE请求失败: {}", endpoint, e);
            throw new EtTestApiException("DELETE请求失败: " + endpoint, e);
        }
    }

    /**
     * 检查响应状态
     */
    private void checkResponse(ResponseEntity<?> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("ET测试API响应异常: 状态码={}, 响应={}",
                    response.getStatusCode(), response.getBody());
            throw new EtTestApiException("API响应异常: " + response.getStatusCode());
        }
    }

    /**
     * 检查API是否启用
     */
    protected void checkApiEnabled() {
        if (!etTestApiConfig.getEnabled()) {
            throw new EtTestApiException("ET测试API服务未启用");
        }

        EtTestApiCategory category = getApiCategory();
        EtTestApiConfig.CategoryConfig categoryConfig =
                etTestApiConfig.getCategoryConfig(category.getCode());

        if (!categoryConfig.getEnabled()) {
            throw new EtTestApiException("ET测试API分类[" + category.getDescription() + "]未启用");
        }
    }
}
