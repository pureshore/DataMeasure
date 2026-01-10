package com.api.agent.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestApiCaseDO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 任务ID
     */
    private Long jobId;

    /**
     * 测试项
     */
    private String testItem;

    /**
     * 用例名称
     */
    private String caseName;

    /**
     * 测试数据
     */
    private String caseData;

    /**
     * 状态
     */
    private String status;

    /**
     * 测试结果
     */
    private String result;

    /**
     * 期望状态码
     */
    private Integer expectCode;

    /**
     * 实际状态码
     */
    private Integer actualCode;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
