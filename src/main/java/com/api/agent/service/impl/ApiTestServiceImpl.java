package com.api.agent.service.impl;

import com.api.agent.dto.TestApiCaseDO;
import com.api.agent.dto.TestApiJobDO;
import com.api.agent.mapper.TestApiCaseMapper;
import com.api.agent.mapper.TestApiJobMapper;
import com.api.agent.service.ApiTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ApiTestServiceImpl implements ApiTestService {
    @Autowired
    private TestApiJobMapper jobMapper;
    @Autowired
    private TestApiCaseMapper caseMapper;
    public TestApiJobDO createTestTask(String apiUrl, MultipartFile excelFile) {
        TestApiJobDO task = new TestApiJobDO();
        task.setCurl(apiUrl);
        task.setJobName(excelFile.getOriginalFilename());
        task.setStatus("PENDING");
//        entityManager.persist(task);
        return task;
    }
    /**
     * 异步执行测试任务
     */
    @Async
    public CompletableFuture<Void> executeTestTaskAsync(Long taskId) {
        try {
            TestApiJobDO task = getTaskById(taskId);
            if (task != null) {
                updateTaskStatus(taskId, "PROCESSING", "测试执行中");

                // 模拟测试执行过程
                performApiTesting(task);

                updateTaskStatus(taskId, "COMPLETED", "测试完成");
            }
        } catch (Exception e) {
            updateTaskStatus(taskId, "FAILED", "测试失败: " + e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 执行API测试
     */
    private void performApiTesting(TestApiJobDO task) {
        try {
            // 这里应该是实际的测试逻辑
            // 模拟测试过程
            Thread.sleep(3000); // 模拟测试耗时

            // 生成测试结果
            String result = "测试完成：共执行10个用例，通过8个，失败2个";
            task.setStatus(result);
            task.setUpdateTime(LocalDateTime.now());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("测试被中断", e);
        }
    }

    /**
     * 获取任务详情
     */
    public TestApiJobDO getTaskById(Long taskId) {
        return jobMapper.selectById(taskId);
    }

    /**
     * 更新任务状态
     */
    @Transactional
    public void updateTaskStatus(Long taskId, String status, String result) {
        TestApiJobDO task = getTaskById(taskId);
        if (task != null) {
            task.setStatus(status);
            if (result != null) {
                task.setStatus(result);
            }
            task.setUpdateTime(LocalDateTime.now());
            jobMapper.update(task);
        }
    }
//
//    /**
//     * 获取最近的任务列表
//     */
//    @SuppressWarnings("unchecked")
//    public List<TestTask> getRecentTasks(int limit) {
//        return entityManager.createQuery(
//                        "SELECT t FROM TestTask t ORDER BY t.createTime DESC", TestTask.class)
//                .setMaxResults(limit)
//                .getResultList();
//    }
//
    /**
     * 获取测试统计信息
     */
    public Map<String, Integer> getTestStats(Long taskId) {
        // 这里应该是从数据库查询实际的测试结果统计
        // 暂时返回模拟数据
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total", 10);
        stats.put("passed", 8);
        stats.put("failed", 2);
        stats.put("blocked", 0);
        return stats;
    }
    public List<TestApiCaseDO> getTestCases(Long taskId) {
        return caseMapper.selectByJobId(taskId);
    }

    public TestApiCaseDO getTestCaseById(Long caseId) {
        return caseMapper.selectById(caseId);
    }

    public void updateTestCase(TestApiCaseDO item) {
        caseMapper.updateById(item);
    }

    /**
     * 保存测试结果到数据库
     */
    @Transactional
    public void saveTestResults(Long taskId, List<Map<String, Object>> testResults) {
        // 这里实现将测试结果保存到数据库的逻辑
        // 根据您的数据库表结构进行实现
        TestApiJobDO task = getTaskById(taskId);
        if (task != null) {
            String resultSummary = generateResultSummary(testResults);
            task.setStatus(resultSummary);
            task.setUpdateTime(LocalDateTime.now());
        }
    }

    /**
     * 生成测试结果摘要
     */
    private String generateResultSummary(List<Map<String, Object>> testResults) {
        long total = testResults.size();
        long passed = testResults.stream().filter(r -> "PASS".equals(r.get("status"))).count();
        long failed = testResults.stream().filter(r -> "FAIL".equals(r.get("status"))).count();
        long errors = testResults.stream().filter(r -> "ERROR".equals(r.get("status"))).count();

        return String.format("测试完成：共执行%d个用例，通过%d个，失败%d个，错误%d个",
                total, passed, failed, errors);
    }
}
