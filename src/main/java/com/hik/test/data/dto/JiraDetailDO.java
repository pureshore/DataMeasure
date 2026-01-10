package com.hik.test.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Accessors(chain = true)
@Data
public class JiraDetailDO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String jiraNumber;
    private String jiraName;
    private String labelName;
    private String productLine;
    private String testOwner;
    private String devOwner;
    private Integer status;
}
