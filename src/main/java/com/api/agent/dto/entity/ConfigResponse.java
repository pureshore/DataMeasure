package com.api.agent.dto.entity;

import lombok.Data;

import java.util.List;

@Data
public class ConfigResponse {
    private boolean success;
    private String message;
    private List<ConfigItem> data;
    private Pagination pagination;

    @Data
    public static class ConfigItem {
        private String key;
        private String label;
        private String type; // text, number, password, url, email
        private String value;
        private String placeholder;
        private Integer min;
        private Integer max;
        private String pattern;
        private String description;
        private boolean required;
        private String layout; // single（一行一个）, triple（三个一行）, double（两个一行）
        private int span; // 占据的列数（1-12）
    }

    @Data
    public static class Pagination {
        private int page;
        private int size;
        private int total;
        private int totalPages;
    }
}