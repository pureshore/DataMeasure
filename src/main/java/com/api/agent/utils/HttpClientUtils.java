package com.api.agent.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {
    public static Map<String, Object> formRequest(String url, String method, Map<String, Object> params, Map<String, String> headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpRequestBase request = null;
        CloseableHttpResponse response = null;

        try {
            // 根据方法类型创建相应的请求对象
            switch (method.toUpperCase()) {
                case "GET":
                    // 构建带参数的URL
                    URIBuilder uriBuilder = new URIBuilder(url);
                    if (params != null) {
                        for (Map.Entry<String, Object> param : params.entrySet()) {
                            uriBuilder.addParameter(param.getKey(), String.valueOf(param.getValue()));
                        }
                    }
                    request = new HttpGet(uriBuilder.build());
                    break;
                case "POST":
                    request = new HttpPost(url);
                    // 设置表单参数
                    if (params != null && !params.isEmpty()) {
                        List<NameValuePair> formParams = new ArrayList<>();
                        for (Map.Entry<String, Object> entry : params.entrySet()) {
                            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                        }
                        ((HttpPost) request).setEntity(new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8));
                    }
                    break;
                case "PUT":
                    request = new HttpPut(url); // 使用HttpPost因为HttpPut构造方式相同
                    // 设置表单参数
                    if (params != null && !params.isEmpty()) {
                        List<NameValuePair> formParams = new ArrayList<>();
                        for (Map.Entry<String, Object> entry : params.entrySet()) {
                            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                        }
                        ((HttpPut) request).setEntity(new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8));
                    }
                    break;
                case "DELETE":
                    // 构建带参数的URL
                    URIBuilder deleteUriBuilder = new URIBuilder(url);
                    if (params != null) {
                        for (Map.Entry<String, Object> param : params.entrySet()) {
                            deleteUriBuilder.addParameter(param.getKey(), String.valueOf(param.getValue()));
                        }
                    }
                    request = new HttpDelete(deleteUriBuilder.build()); // Apache HttpClient没有HttpDelete带实体的实现
                    break;
                default:
                    throw new IllegalArgumentException("不支持的请求方法: " + method);
            }

            // 添加请求头
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    request.setHeader(header.getKey(), header.getValue());
                }
            }
            // 执行请求
            response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            Map<String, Object> result = new HashMap<>();
            result.put("status", response.getStatusLine().getStatusCode());
            result.put("response", responseBody);
            return result;
        } catch (Exception e) {
            System.out.println("请求失败: " + e.getMessage());
            return null;
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                // 忽略关闭异常
            }
        }
    }


    /**
     * 发送JSON格式的HTTP请求（支持POST、GET、PUT、DELETE等方法）
     *
     * @param url     请求URL
     * @param method  HTTP方法（POST、GET、PUT、DELETE等）
     * @param json    JSON字符串
     * @param headers 请求头
     * @return 请求结果
     */
    public static Map<String, Object> sendJsonRequest(String url, String method, String json, Map<String, String> headers) {
        if (method == null) {
            throw new RuntimeException("请求方法不能为空");
        }

        if (url == null) {
            throw new RuntimeException("URL不能为空");
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpRequestBase request = null;
        CloseableHttpResponse response = null;

        try {
            // 根据方法类型创建相应的请求对象
            switch (method.toUpperCase()) {
                case "GET":
                    request = new HttpGet(url);
                    break;
                case "POST":
                    HttpPost postRequest = new HttpPost(url);
                    // 设置JSON请求体
                    if (json != null) {
                        StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
                        entity.setContentType("application/json");
                        postRequest.setEntity(entity);
                    }
                    request = postRequest;
                    break;
                case "PUT":
                    HttpPut putRequest = new HttpPut(url);
                    // 设置JSON请求体
                    if (json != null) {
                        StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
                        entity.setContentType("application/json");
                        putRequest.setEntity(entity);
                    }
                    request = putRequest;
                    break;
                case "DELETE":
                    request = new HttpDelete(url);
                    break;
                default:
                    throw new RuntimeException("不支持的请求方法: " + method);
            }

            // 添加请求头
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    request.setHeader(header.getKey(), header.getValue());
                }
            }

            // 设置JSON内容类型（对于需要请求体的方法）
            if (("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) && json != null) {
                request.setHeader("Content-Type", "application/json; charset=utf-8");
            }

            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String responseBody = entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : null;
            Map<String, Object> result = new HashMap<>();
            result.put("status", response.getStatusLine().getStatusCode());
            result.put("response", responseBody);
            return result;
        } catch (IOException e) {
            throw new RuntimeException("HTTP请求失败", e);
        } catch (Exception e) {
            throw new RuntimeException("HTTP请求失败: " + e.getMessage(), e);
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                // 忽略关闭异常
            }
        }
    }
}
