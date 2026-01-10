package com.api.agent.mapper;

import com.api.agent.dto.TestApiCaseDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestApiCaseMapper {
    void insert(TestApiCaseDO jobDO);
    void updateById(TestApiCaseDO student);
    void delete(Long id);
    TestApiCaseDO selectById(Long id);
    List<TestApiCaseDO> selectByJobId(Long jobId);
}