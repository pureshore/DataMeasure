package com.hik.test.data.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hik.test.data.dto.JiraDetailDO;
import com.hik.test.data.enums.JiraTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class JiraIssueDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("key")
    private String key;

    @JsonProperty("self")
    private String self;

    @JsonProperty("fields")
    private Fields fields;

    @Data
    public static class Fields {

        @JsonProperty("summary")
        private String summary;

        @JsonProperty("description")
        private String description;

        @JsonProperty("labels")
        private List<String> labels;

        @JsonProperty("issuetype")
        private IssueType issuetype;

        @JsonProperty("status")
        private Status status;

        @JsonProperty("assignee")
        private Person assignee;

        @JsonProperty("reporter")
        private Person reporter;

        @JsonProperty("created")
        private Date created;

        @JsonProperty("updated")
        private Date updated;

        @JsonProperty("customfield_10000") // 示例：自定义字段-产品线
        private String productLine;

        @JsonProperty("customfield_10001") // 示例：自定义字段-测试负责人
        private String testOwner;

        @JsonProperty("customfield_10002") // 示例：自定义字段-开发负责人
        private String devOwner;
    }

    @Data
    public static class IssueType {

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("subtask")
        private Boolean subtask;
    }

    @Data
    public static class Status {

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;
    }

    @Data
    public static class Person {

        @JsonProperty("displayName")
        private String displayName;

        @JsonProperty("emailAddress")
        private String emailAddress;
    }

    /**
     * 转换为内部JiraDetail实体
     */
    public JiraDetailDO toJiraDetail() {
        JiraDetailDO detail = new JiraDetailDO();

        detail.setJiraNo(this.key);
        detail.setJiraName(this.fields != null ? this.fields.getSummary() : "");
        detail.setJiraType(JiraTypeEnum.BUGFIX.name()); // 默认为Bugfix类型
        detail.setLabels(this.fields != null ? this.fields.getLabels() : null);
        detail.setCreateTime(this.fields != null ? this.fields.getCreated() : new Date());
        detail.setUpdateTime(this.fields != null ? this.fields.getUpdated() : new Date());

        // 设置产品线和负责人（从自定义字段获取）
        if (this.fields != null) {
            detail.setProductLine(this.fields.getProductLine());
            detail.setTestOwner(this.fields.getTestOwner());
            detail.setDevOwner(this.fields.getDevOwner());
        }

        detail.setStatus(1);

        return detail;
    }

    /**
     * 获取负责人姓名（开发或测试）
     */
    public String getAssigneeName() {
        if (fields != null && fields.getAssignee() != null) {
            return fields.getAssignee().getDisplayName();
        }
        return null;
    }

    /**
     * 获取报告人姓名
     */
    public String getReporterName() {
        if (fields != null && fields.getReporter() != null) {
            return fields.getReporter().getDisplayName();
        }
        return null;
    }
}
