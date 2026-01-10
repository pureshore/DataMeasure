package com.api.agent.service;

import com.api.agent.dto.TestApiCaseDO;
import com.api.agent.dto.TestApiJobDO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ApiTestService {
    TestApiJobDO createTestTask(String apiUrl, MultipartFile excelFile);
    CompletableFuture<Void> executeTestTaskAsync(Long taskId);
    TestApiJobDO getTaskById(Long taskId);
    List<TestApiCaseDO> getTestCases(Long taskId);
    TestApiCaseDO getTestCaseById(Long caseId);
    void updateTestCase(TestApiCaseDO item);
}
