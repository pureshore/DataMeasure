package com.hik.test.data.client;

import com.hik.test.data.client.base.BaseEtTestApiClient;
import com.hik.test.data.config.EtTestApiConfig;
import com.hik.test.data.dto.TestCase;
import com.hik.test.data.enums.EtTestApiCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * 测试用例类API客户端
 */
@Slf4j
@Component
public class TestCasesApiClient extends BaseEtTestApiClient {

    public TestCasesApiClient(RestTemplate etTestRestTemplate, EtTestApiConfig etTestApiConfig) {
        super(etTestRestTemplate, etTestApiConfig);
    }

    @Override
    protected EtTestApiCategory getApiCategory() {
        return EtTestApiCategory.TEST_CASES;
    }


    /**
     * 3. 获取测试用例详情
     */
    public Object getTestCase(String testCaseId) {
        checkApiEnabled();

        String endpoint = "/{testCaseId}";
        return get(endpoint, Object.class, Map.of("testCaseId", testCaseId));
    }



    /**
     * 6. 分页查询测试用例
     */
//    public Object queryTestCases(TestCaseQuery query) {
//        checkApiEnabled();
//
//        String endpoint = "/query";
////        return get(endpoint, new org.springframework.core.ParameterizedTypeReference<List<TestCase>>() {},
////                Map.of("requirementId", requirementId));
//        return post(endpoint, query, Object.class);
//    }

    /**
     * 7. 根据需求ID获取测试用例
     */
//    public List<TestCase> getTestCasesByRequirementId(String requirementId) {
//        checkApiEnabled();
//
//        String endpoint = "/by-requirement/{requirementId}";
//        return get(endpoint,  new org.springframework.core.ParameterizedTypeReference<List<TestCase>>() {},
//                Map.of("requirementId", requirementId));
//    }

//    /**
//     * 8. 获取测试用例执行历史
//     */
//    public List<TestCaseExecutionHistory> getExecutionHistory(String testCaseId, Integer limit) {
//        checkApiEnabled();
//
//        String endpoint = "/{testCaseId}/execution-history";
//        Map<String, Object> params = Map.of(
//                "testCaseId", testCaseId,
//                "limit", limit
//        );
//
//        return get(endpoint, new org.springframework.core.ParameterizedTypeReference<List<TestCaseExecutionHistory>>() {}, params);
//    }
//

}
