package com.hik.test.data.client;

import com.hik.test.data.client.base.BaseEtTestApiClient;
import com.hik.test.data.config.EtTestApiConfig;
import com.hik.test.data.enums.EtTestApiCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 测试集类API客户端
 */
@Slf4j
@Component
public class TestSuitesApiClient extends BaseEtTestApiClient {

    public TestSuitesApiClient(RestTemplate etTestRestTemplate, EtTestApiConfig etTestApiConfig) {
        super(etTestRestTemplate, etTestApiConfig);
    }

    @Override
    protected EtTestApiCategory getApiCategory() {
        return EtTestApiCategory.TEST_SUITES;
    }


    /**
     * 2. 获取测试集详情
     */
    public Object getTestSuite(String testSuiteId) {
        checkApiEnabled();

        String endpoint = "/{testSuiteId}";
        return get(endpoint, Object.class, Map.of("testSuiteId", testSuiteId));
    }


    /**
     * 8. 获取测试集中的测试用例
     */
    public Object getTestCasesInSuite(String testSuiteId, Boolean includeDetails) {
        checkApiEnabled();

        String endpoint = "/{testSuiteId}/test-cases";
        Map<String, Object> params = Map.of(
                "testSuiteId", testSuiteId,
                "includeDetails", includeDetails
        );

        return get(endpoint, Object.class, params);
    }

}
