package com.hik.test.data.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class JiraApiResponse {

    @JsonProperty("expand")
    private String expand;

    @JsonProperty("startAt")
    private Integer startAt;

    @JsonProperty("maxResults")
    private Integer maxResults;

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("issues")
    private List<JiraIssueDTO> issues;
}
