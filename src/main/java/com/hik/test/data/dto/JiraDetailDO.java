package com.hik.test.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Accessors(chain = true)
@Data
public class JiraDetailDO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String jiraNo;
    private String jiraName;
    private String jiraType;
    private List<String> labels;
    private String labelName;
    private String productLine;
    private String testOwner;
    private String devOwner;
    private Integer status;
}
