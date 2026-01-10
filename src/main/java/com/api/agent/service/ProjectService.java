package com.api.agent.service;

import com.api.agent.dto.entity.ProjectSyncResult;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

    private final RestTemplate restTemplate;

    public ProjectService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * 同步项目信息
     */
    public ProjectSyncResult syncProject(String projectId) {
        try {
            // 调用外部接口A获取项目信息
            String apiUrl = "https://api.example.com/project/" + projectId + "/info";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer your-token-here"); // 根据实际情况调整
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> projectData = response.getBody();

                ProjectSyncResult.ProjectData data = new ProjectSyncResult.ProjectData();
                data.setProjectId(projectId);
                data.setProjectName((String) projectData.get("name"));
                data.setApiCount((Integer) projectData.get("apiCount"));
                data.setApiUrl((String) projectData.get("apiUrl"));
                data.setSyncTime(LocalDateTime.now());

                return new ProjectSyncResult(true, "同步成功", data);
            } else {
                return new ProjectSyncResult(false, "接口返回数据异常");
            }

        } catch (HttpClientErrorException.NotFound e) {
            return new ProjectSyncResult(false, "项目不存在或ID错误");
        } catch (HttpClientErrorException.Unauthorized e) {
            return new ProjectSyncResult(false, "认证失败，请检查访问权限");
        } catch (ResourceAccessException e) {
            return new ProjectSyncResult(false, "网络连接失败，请检查接口地址");
        } catch (Exception e) {
            return new ProjectSyncResult(false, "同步失败: " + e.getMessage());
        }
    }

    /**
     * 模拟同步方法（用于测试）
     */
    public ProjectSyncResult syncProjectMock(String projectId) {
        try {
            // 模拟网络延迟
            Thread.sleep(1000);

            // 模拟不同的项目ID返回不同的数据
            ProjectSyncResult.ProjectData data = new ProjectSyncResult.ProjectData();
            data.setProjectId(projectId);

            switch (projectId) {
                case "1001":
                    data.setProjectName("用户管理系统");
                    data.setApiCount(15);
                    data.setApiUrl("https://api.example.com/user/v1");
                    break;
                case "1002":
                    data.setProjectName("订单处理系统");
                    data.setApiCount(12);
                    data.setApiUrl("https://api.example.com/order/v1");
                    break;
                case "1003":
                    data.setProjectName("支付网关系统");
                    data.setApiCount(8);
                    data.setApiUrl("https://api.example.com/payment/v1");
                    break;
                default:
                    data.setProjectName("测试项目-" + projectId);
                    data.setApiCount(5);
                    data.setApiUrl("https://api.example.com/test/v1");
            }

            data.setSyncTime(LocalDateTime.now());

            return new ProjectSyncResult(true, "同步成功", data);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ProjectSyncResult(false, "同步被中断");
        }
    }

    // 在ProjectService中添加方法
    /**
     * 获取项目URL列表（独立接口）
     */
    public ProjectSyncResult getProjectUrls(String projectId) {
        try {
            // 模拟从接口获取URL列表
            return mockProjectUrls(projectId);

        } catch (Exception e) {
            ProjectSyncResult response = new ProjectSyncResult(true,"success");
            response.setSuccess(false);
            response.setMessage("获取URL列表失败: " + e.getMessage());
            return response;
        }
    }

    /**
     * 模拟项目URL数据
     */
    private ProjectSyncResult mockProjectUrls(String projectId) {
        ProjectSyncResult.ProjectData data = new ProjectSyncResult.ProjectData();
        data.setProjectId(projectId);

        // 根据项目ID返回不同的URL列表
        List<String> apiUrls;
        String projectName;

        switch (projectId) {
            case "1001":
                projectName = "用户管理系统";
                apiUrls = Arrays.asList(
                        "https://api.user.com/v1/login",
                        "https://api.user.com/v1/register",
                        "https://api.user.com/v1/profile/get",
                        "https://api.user.com/v1/profile/update",
                        "https://api.user.com/v1/users/list",
                        "https://api.user.com/v1/users/search"
                );
                break;
            case "1002":
                projectName = "订单处理系统";
                apiUrls = Arrays.asList(
                        "https://api.order.com/v1/orders/create",
                        "https://api.order.com/v1/orders/list",
                        "https://api.order.com/v1/orders/detail",
                        "https://api.order.com/v1/orders/update",
                        "https://api.order.com/v1/orders/cancel",
                        "https://api.order.com/v1/payments/process"
                );
                break;
            case "1003":
                projectName = "支付网关系统";
                apiUrls = Arrays.asList(
                        "https://api.payment.com/v1/payments/create",
                        "https://api.payment.com/v1/payments/query",
                        "https://api.payment.com/v1/payments/refund",
                        "https://api.payment.com/v1/payments/notify",
                        "https://api.payment.com/v1/transactions/history"
                );
                break;
            default:
                projectName = "通用项目-" + projectId;
                apiUrls = Arrays.asList(
                        "https://api.example.com/v1/endpoint1",
                        "https://api.example.com/v1/endpoint2",
                        "https://api.example.com/v1/endpoint3",
                        "https://api.example.com/v1/endpoint4"
                );
        }

        data.setProjectName(projectName);
        data.setApiUrls(apiUrls);
        data.setApiCount(apiUrls.size());
        data.setSyncTime(LocalDateTime.now());

        ProjectSyncResult response = new ProjectSyncResult(true,"success");
        response.setSuccess(true);
        response.setMessage("获取URL列表成功");
        response.setData(data);

        return response;
    }
}
