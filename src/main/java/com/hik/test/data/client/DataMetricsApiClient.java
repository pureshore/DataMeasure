package com.hik.test.data.client;

import com.hik.test.data.client.base.BaseEtTestApiClient;
import com.hik.test.data.config.EtTestApiConfig;
import com.hik.test.data.enums.EtTestApiCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数据度量类API客户端
 */
@Slf4j
@Component
public class DataMetricsApiClient extends BaseEtTestApiClient {

    public DataMetricsApiClient(RestTemplate etTestRestTemplate, EtTestApiConfig etTestApiConfig) {
        super(etTestRestTemplate, etTestApiConfig);
    }

    @Override
    protected EtTestApiCategory getApiCategory() {
        return EtTestApiCategory.DATA_METRICS;
    }

//    /**
//     * 1. 获取测试覆盖率数据
//     */
//    public TestCoverageMetrics getTestCoverage(String projectId, LocalDate startDate, LocalDate endDate) {
//        checkApiEnabled();
//
//        String endpoint = "/coverage";
//        Map<String, Object> params = Map.of(
//                "projectId", projectId,
//                "startDate", startDate.toString(),
//                "endDate", endDate.toString()
//        );
//
//        return get(endpoint, TestCoverageMetrics.class, params);
//    }
//
//    /**
//     * 2. 获取缺陷密度数据
//     */
//    public DefectDensityMetrics getDefectDensity(String moduleId, Integer periodDays) {
//        checkApiEnabled();
//
//        String endpoint = "/defect-density";
//        Map<String, Object> params = Map.of(
//                "moduleId", moduleId,
//                "periodDays", periodDays
//        );
//
//        return get(endpoint, DefectDensityMetrics.class, params);
//    }
//
//    /**
//     * 3. 获取代码质量指标
//     */
//    public CodeQualityMetrics getCodeQualityMetrics(String repoUrl, String branch) {
//        checkApiEnabled();
//
//        String endpoint = "/code-quality";
//        Map<String, Object> params = Map.of(
//                "repoUrl", repoUrl,
//                "branch", branch
//        );
//
//        return get(endpoint, CodeQualityMetrics.class, params);
//    }
//
//    /**
//     * 4. 获取性能测试数据
//     */
//    public PerformanceMetrics getPerformanceMetrics(String testSuiteId, LocalDateTime startTime, LocalDateTime endTime) {
//        checkApiEnabled();
//
//        String endpoint = "/performance";
//        Map<String, Object> params = Map.of(
//                "testSuiteId", testSuiteId,
//                "startTime", startTime.toString(),
//                "endTime", endTime.toString()
//        );
//
//        return get(endpoint, PerformanceMetrics.class, params);
//    }
//
//    /**
//     * 5. 获取测试执行趋势
//     */
//    public TestExecutionTrend getTestExecutionTrend(String projectId, String trendType, Integer dataPoints) {
//        checkApiEnabled();
//
//        String endpoint = "/execution-trend";
//        Map<String, Object> params = Map.of(
//                "projectId", projectId,
//                "trendType", trendType,
//                "dataPoints", dataPoints
//        );
//
//        return get(endpoint, TestExecutionTrend.class, params);
//    }
//
//    /**
//     * 6. 获取团队生产力指标
//     */
//    public TeamProductivityMetrics getTeamProductivity(String teamId, String period) {
//        checkApiEnabled();
//
//        String endpoint = "/team-productivity";
//        Map<String, Object> params = Map.of(
//                "teamId", teamId,
//                "period", period
//        );
//
//        return get(endpoint, TeamProductivityMetrics.class, params);
//    }
//
//    /**
//     * 7. 批量获取指标数据
//     */
//    public List<MetricData> batchGetMetrics(List<MetricQuery> queries) {
//        checkApiEnabled();
//
//        String endpoint = "/batch";
//        return post(endpoint, queries, new org.springframework.core.ParameterizedTypeReference<List<MetricData>>() {});
//    }
//
//    /**
//     * 8. 生成度量报告
//     */
//    public MetricReport generateMetricReport(ReportRequest request) {
//        checkApiEnabled();
//
//        String endpoint = "/reports/generate";
//        return post(endpoint, request, MetricReport.class);
//    }
}