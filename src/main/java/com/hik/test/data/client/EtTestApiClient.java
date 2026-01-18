package com.hik.test.data.client;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ET测试API聚合客户端
 * 提供统一的API访问入口，内部委托给各分类客户端
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EtTestApiClient {

    private final DataMetricsApiClient dataMetricsApiClient;
    private final TestCasesApiClient testCasesApiClient;
    private final TestSuitesApiClient testSuitesApiClient;

//    // ==================== 数据度量类API ====================
//
//    /**
//     * 获取测试覆盖率数据
//     */
//    public TestCoverageMetrics getTestCoverage(String projectId, LocalDate startDate, LocalDate endDate) {
//        return dataMetricsApiClient.getTestCoverage(projectId, startDate, endDate);
//    }
//
//    /**
//     * 获取缺陷密度数据
//     */
//    public DefectDensityMetrics getDefectDensity(String moduleId, Integer periodDays) {
//        return dataMetricsApiClient.getDefectDensity(moduleId, periodDays);
//    }
//
//    /**
//     * 获取代码质量指标
//     */
//    public CodeQualityMetrics getCodeQualityMetrics(String repoUrl, String branch) {
//        return dataMetricsApiClient.getCodeQualityMetrics(repoUrl, branch);
//    }
//
//    /**
//     * 获取性能测试数据
//     */
//    public PerformanceMetrics getPerformanceMetrics(String testSuiteId, LocalDateTime startTime, LocalDateTime endTime) {
//        return dataMetricsApiClient.getPerformanceMetrics(testSuiteId, startTime, endTime);
//    }
//
//    // ==================== 测试用例类API ====================
//
//    /**
//     * 创建测试用例
//     */
//    public TestCase createTestCase(TestCaseCreateRequest request) {
//        return testCasesApiClient.createTestCase(request);
//    }
//
//    /**
//     * 获取测试用例详情
//     */
//    public TestCase getTestCase(String testCaseId) {
//        return testCasesApiClient.getTestCase(testCaseId);
//    }
//
//    /**
//     * 分页查询测试用例
//     */
//    public Page<TestCase> queryTestCases(TestCaseQuery query) {
//        return testCasesApiClient.queryTestCases(query);
//    }
//
//    /**
//     * 根据需求ID获取测试用例
//     */
//    public List<TestCase> getTestCasesByRequirementId(String requirementId) {
//        return testCasesApiClient.getTestCasesByRequirementId(requirementId);
//    }
//
//    // ==================== 测试集类API ====================
//
//    /**
//     * 创建测试集
//     */
//    public TestSuite createTestSuite(TestSuiteCreateRequest request) {
//        return testSuitesApiClient.createTestSuite(request);
//    }
//
//    /**
//     * 获取测试集详情
//     */
//    public TestSuite getTestSuite(String testSuiteId) {
//        return testSuitesApiClient.getTestSuite(testSuiteId);
//    }
//
//    /**
//     * 向测试集添加测试用例
//     */
//    public TestSuite addTestCasesToSuite(String testSuiteId, List<String> testCaseIds) {
//        return testSuitesApiClient.addTestCasesToSuite(testSuiteId, testCaseIds);
//    }
//
//    /**
//     * 获取测试集中的测试用例
//     */
//    public List<TestCase> getTestCasesInSuite(String testSuiteId, Boolean includeDetails) {
//        return testSuitesApiClient.getTestCasesInSuite(testSuiteId, includeDetails);
//    }
//
//    /**
//     * 执行测试集
//     */
//    public TestSuiteExecutionResult executeTestSuite(String testSuiteId, TestSuiteExecutionRequest request) {
//        return testSuitesApiClient.executeTestSuite(testSuiteId, request);
//    }
//
//    // ==================== 组合操作 ====================
//
//    /**
//     * 创建测试集并添加测试用例
//     */
//    public TestSuite createTestSuiteWithCases(TestSuiteCreateRequest suiteRequest, List<String> testCaseIds) {
//        // 1. 创建测试集
//        TestSuite testSuite = createTestSuite(suiteRequest);
//
//        // 2. 添加测试用例
//        if (testCaseIds != null && !testCaseIds.isEmpty()) {
//            return addTestCasesToSuite(testSuite.getId(), testCaseIds);
//        }
//
//        return testSuite;
//    }
//
//    /**
//     * 批量创建测试用例并添加到测试集
//     */
//    public List<TestCase> createTestCasesAndAddToSuite(String testSuiteId, List<TestCaseCreateRequest> requests) {
//        // 1. 批量创建测试用例
//        List<TestCase> createdCases = testCasesApiClient.batchCreateTestCases(requests);
//
//        // 2. 获取测试用例ID列表
//        List<String> testCaseIds = createdCases.stream()
//                .map(TestCase::getId)
//                .toList();
//
//        // 3. 添加到测试集
//        testSuitesApiClient.addTestCasesToSuite(testSuiteId, testCaseIds);
//
//        return createdCases;
//    }
//
//    /**
//     * 获取测试集的完整度量数据
//     */
//    public TestSuiteCompleteMetrics getTestSuiteCompleteMetrics(String testSuiteId) {
//        // 1. 获取测试集详情
//        TestSuite testSuite = getTestSuite(testSuiteId);
//
//        // 2. 获取测试集中的测试用例
//        List<TestCase> testCases = getTestCasesInSuite(testSuiteId, true);
//
//        // 3. 获取测试集执行历史（如果有）
//        List<TestSuiteExecutionResult> executionResults = testSuitesApiClient
//                .getRecentExecutionResults(testSuiteId, 5);
//
//        // 4. 获取测试覆盖率数据
//        TestCoverageMetrics coverage = null;
//        if (testSuite.getProjectId() != null) {
//            coverage = getTestCoverage(testSuite.getProjectId(),
//                    LocalDate.now().minusDays(30), LocalDate.now());
//        }
//
//        return new TestSuiteCompleteMetrics(testSuite, testCases, executionResults, coverage);
//    }
}
