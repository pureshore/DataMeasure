package com.hik.test.data.dto.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectSyncResult {
    private boolean success;
    private String message;
    private ProjectData data;

    // 构造方法
    public ProjectSyncResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ProjectSyncResult(boolean success, String message, ProjectData data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    @Data
    public static class ProjectData {
        private String projectId;
        private String projectName;
        private Integer apiCount;
        private String apiUrl;
        private List<String> apiUrls;
        private LocalDateTime syncTime;
    }
}