package com.api.agent.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestApiJobDO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 用例总数
     */
    private Integer caseCount;

    /**
     * 状态
     */
    private String status;

    /**
     * 请求测试curl
     */
    private String curl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
