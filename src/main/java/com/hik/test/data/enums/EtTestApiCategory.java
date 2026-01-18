package com.hik.test.data.enums;

import lombok.Getter;

/**
 * ET测试API分类枚举
 */
@Getter
public enum EtTestApiCategory {

    DATA_METRICS("data-metrics", "数据度量类API", "/api/v1/data-metrics"),
    TEST_CASES("test-cases", "测试用例类API", "/api/v1/test-cases"),
    TEST_SUITES("test-suites", "测试集类API", "/api/v1/test-suites"),
    TEST_EXECUTIONS("test-executions", "测试执行类API", "/api/v1/test-executions"),
    DEFECTS("defects", "缺陷管理类API", "/api/v1/defects"),
    REPORTS("reports", "测试报告类API", "/api/v1/reports"),
    CONFIGURATIONS("configurations", "配置管理类API", "/api/v1/configurations");

    private final String code;
    private final String description;
    private final String basePath;

    EtTestApiCategory(String code, String description, String basePath) {
        this.code = code;
        this.description = description;
        this.basePath = basePath;
    }

    /**
     * 根据编码获取分类
     */
    public static EtTestApiCategory fromCode(String code) {
        for (EtTestApiCategory category : values()) {
            if (category.getCode().equals(code)) {
                return category;
            }
        }
        throw new IllegalArgumentException("未知的API分类: " + code);
    }

    /**
     * 获取所有分类的编码列表
     */
    public static String[] getAllCodes() {
        return java.util.Arrays.stream(values())
                .map(EtTestApiCategory::getCode)
                .toArray(String[]::new);
    }
}
