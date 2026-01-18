package com.hik.test.data.client.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ET测试API异常基类
 */
@Getter
public class EtTestApiException extends RuntimeException {

    /**
     * 错误码
     */
    private final String errorCode;

    /**
     * HTTP状态码
     */
    private final HttpStatus httpStatus;

    /**
     * 请求ID（用于追踪）
     */
    private final String requestId;

    /**
     * API分类
     */
    private final String apiCategory;

    /**
     * 请求URL
     */
    private final String requestUrl;

    /**
     * 原始响应数据（如果有）
     */
    private final String rawResponse;

    /**
     * 构造方法
     */
    public EtTestApiException(String message) {
        super(message);
        this.errorCode = "ET_API_0001";
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.requestId = null;
        this.apiCategory = null;
        this.requestUrl = null;
        this.rawResponse = null;
    }

    public EtTestApiException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ET_API_0001";
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.requestId = null;
        this.apiCategory = null;
        this.requestUrl = null;
        this.rawResponse = null;
    }

    public EtTestApiException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.requestId = null;
        this.apiCategory = null;
        this.requestUrl = null;
        this.rawResponse = null;
    }

    public EtTestApiException(String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.requestId = null;
        this.apiCategory = null;
        this.requestUrl = null;
        this.rawResponse = null;
    }

    public EtTestApiException(String errorCode, String message, HttpStatus httpStatus,
                              Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.requestId = null;
        this.apiCategory = null;
        this.requestUrl = null;
        this.rawResponse = null;
    }

    public EtTestApiException(String errorCode, String message, HttpStatus httpStatus,
                              String requestId, String apiCategory, String requestUrl) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.requestId = requestId;
        this.apiCategory = apiCategory;
        this.requestUrl = requestUrl;
        this.rawResponse = null;
    }

    public EtTestApiException(String errorCode, String message, HttpStatus httpStatus,
                              String requestId, String apiCategory, String requestUrl,
                              String rawResponse) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.requestId = requestId;
        this.apiCategory = apiCategory;
        this.requestUrl = requestUrl;
        this.rawResponse = rawResponse;
    }

    @Override
    public String toString() {
        return String.format("EtTestApiException{errorCode='%s', httpStatus=%s, " +
                        "requestId='%s', apiCategory='%s', requestUrl='%s', message='%s'}",
                errorCode, httpStatus, requestId, apiCategory, requestUrl, getMessage());
    }

    /**
     * 获取详细的错误信息
     */
    public String getDetailedMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("ET测试API异常: \n");
        sb.append("  错误码: ").append(errorCode).append("\n");
        sb.append("  消息: ").append(getMessage()).append("\n");
        sb.append("  HTTP状态: ").append(httpStatus).append("\n");

        if (requestId != null) {
            sb.append("  请求ID: ").append(requestId).append("\n");
        }

        if (apiCategory != null) {
            sb.append("  API分类: ").append(apiCategory).append("\n");
        }

        if (requestUrl != null) {
            sb.append("  请求URL: ").append(requestUrl).append("\n");
        }

        if (rawResponse != null && !rawResponse.isEmpty()) {
            sb.append("  原始响应: ").append(rawResponse).append("\n");
        }

        if (getCause() != null) {
            sb.append("  根因: ").append(getCause().getMessage()).append("\n");
        }

        return sb.toString();
    }
}