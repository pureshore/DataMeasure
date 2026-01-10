package com.hik.test.data.client;

import com.hik.test.data.config.JiraConfig;
import com.hik.test.data.dto.external.JiraApiResponse;
import com.hik.test.data.dto.external.JiraIssueDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JiraApiClient {

    private final RestTemplate restTemplate;
    private final JiraConfig jiraConfig;

    /**
     * 获取JIRA系统中的Bugfix类型工单
     * @return Bugfix类型的JIRA工单列表
     */
    public List<JiraIssueDTO> getBugfixJiraIssues() {
        try {
            // 构建JQL查询语句：查询Bugfix类型且未关闭的工单
            String jql = String.format(
                    "project = '%s' AND issuetype = Bug AND status NOT IN (Closed, Resolved) ORDER BY created DESC",
                    jiraConfig.getProjectKey()
            );

            // 构建请求URL
            String url = UriComponentsBuilder.fromHttpUrl(jiraConfig.getBaseUrl() + "/rest/api/2/search")
                    .queryParam("jql", jql)
                    .queryParam("startAt", 0)
                    .queryParam("maxResults", 100) // 每次最多获取100条
                    .queryParam("fields", "key,summary,labels,issuetype,status,assignee,reporter,created,updated")
                    .toUriString();

            // 构建请求头
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 发送请求
            log.info("调用JIRA API查询Bugfix类型工单，JQL: {}", jql);
            ResponseEntity<JiraApiResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, JiraApiResponse.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JiraApiResponse apiResponse = response.getBody();
                log.info("成功获取Bugfix类型工单，总数: {}", apiResponse.getTotal());
                return apiResponse.getIssues();
            }

            log.warn("获取Bugfix类型工单失败，状态码: {}", response.getStatusCode());
            return Collections.emptyList();

        } catch (Exception e) {
            log.error("调用JIRA API查询Bugfix类型工单失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * 根据JIRA单号获取详情
     * @param jiraKey JIRA单号（如：PROJ-123）
     * @return JIRA工单详情
     */
    public JiraIssueDTO getJiraIssueDetail(String jiraKey) {
        try {
            String url = jiraConfig.getBaseUrl() + "/rest/api/2/issue/" + jiraKey;

            // 构建请求头
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            log.info("调用JIRA API获取工单详情，单号: {}", jiraKey);
            ResponseEntity<JiraIssueDTO> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, JiraIssueDTO.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("成功获取工单详情: {}", jiraKey);
                return response.getBody();
            }

            log.warn("获取工单详情失败，单号: {}, 状态码: {}", jiraKey, response.getStatusCode());
            return null;

        } catch (Exception e) {
            log.error("调用JIRA API获取工单详情失败，单号: {}，错误: {}", jiraKey, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 批量获取JIRA工单详情
     * @param jiraKeys JIRA单号列表
     * @return JIRA工单详情列表
     */
    public List<JiraIssueDTO> getJiraIssuesDetail(List<String> jiraKeys) {
        if (jiraKeys == null || jiraKeys.isEmpty()) {
            return Collections.emptyList();
        }

        List<JiraIssueDTO> results = new ArrayList<>();
        for (String jiraKey : jiraKeys) {
            try {
                JiraIssueDTO detail = getJiraIssueDetail(jiraKey);
                if (detail != null) {
                    results.add(detail);
                }
                // 避免请求频率过高
                Thread.sleep(100);
            } catch (Exception e) {
                log.error("获取工单详情异常，单号: {}", jiraKey, e);
            }
        }

        return results;
    }

    /**
     * 获取JIRA系统中的所有Bug类型（包含子类型）
     * @return Bug类型列表
     */
    public List<String> getBugIssueTypes() {
        try {
            String url = jiraConfig.getBaseUrl() + "/rest/api/2/issuetype";

            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity,
                    new org.springframework.core.ParameterizedTypeReference<List<Map<String, Object>>>() {});

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().stream()
                        .filter(type -> "Bug".equals(type.get("name")))
                        .map(type -> type.get("id").toString())
                        .collect(Collectors.toList());
            }

            return Collections.emptyList();

        } catch (Exception e) {
            log.error("获取JIRA问题类型失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * 创建请求头（包含认证信息）
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // JIRA认证（Basic Auth）
        String auth = jiraConfig.getUsername() + ":" + jiraConfig.getPassword();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);

        return headers;
    }
}
