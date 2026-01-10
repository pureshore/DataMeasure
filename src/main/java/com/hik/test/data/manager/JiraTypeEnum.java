package com.hik.test.data.manager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum JiraTypeEnum {
    COMPLAINT("COMPLAINT", "客诉单"),
    BUGFIX("BUGFIX", "Bug修复"),
    DELAY("DELAY", "延期缺陷"),
    FEATURE("FEATURE", "新功能"),
    TASK("TASK", "任务"),
    IMPROVEMENT("IMPROVEMENT", "优化"),
    OTHER("OTHER", "其他");

    @JsonValue
    private final String code;
    private final String description;

    private static final Map<String, JiraTypeEnum> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(JiraTypeEnum::getCode, Function.identity()));

    JiraTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据编码获取枚举
     */
    @JsonCreator
    public static JiraTypeEnum fromCode(String code) {
        return CODE_MAP.get(code.toUpperCase());
    }

    /**
     * 检查编码是否有效
     */
    public static boolean isValid(String code) {
        return code != null && CODE_MAP.containsKey(code.toUpperCase());
    }

    /**
     * 获取所有枚举的编码列表
     */
    public static String[] getAllCodes() {
        return Arrays.stream(values())
                .map(JiraTypeEnum::getCode)
                .toArray(String[]::new);
    }

    /**
     * 获取所有枚举的描述列表
     */
    public static String[] getAllDescriptions() {
        return Arrays.stream(values())
                .map(JiraTypeEnum::getDescription)
                .toArray(String[]::new);
    }

    /**
     * 是否是缺陷类类型（客诉单、Bug修复、延期缺陷）
     */
    public boolean isDefectType() {
        return this == COMPLAINT || this == BUGFIX || this == DELAY;
    }

    /**
     * 是否是功能类类型（新功能、优化）
     */
    public boolean isFeatureType() {
        return this == FEATURE || this == IMPROVEMENT;
    }
}
