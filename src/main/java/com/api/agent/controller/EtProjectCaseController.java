package com.api.agent.controller;

import com.api.agent.dto.entity.ProjectSyncResult;
import com.api.agent.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
public class EtProjectCaseController {
    @Autowired
    private ProjectService projectService;
    @GetMapping("/sync")
    public ProjectSyncResult syncProject(@RequestParam String projectId) {
        // 使用模拟方法，实际使用时可以切换为真实接口调用
        return projectService.syncProjectMock(projectId);

        // 真实接口调用（需要配置正确的接口地址和认证信息）
        // return projectService.syncProject(projectId);
    }

    @GetMapping("/urls")
    public ProjectSyncResult getProjectUrls(@RequestParam String projectId) {
        // 独立的URL列表接口
        return projectService.getProjectUrls(projectId);
    }
}
