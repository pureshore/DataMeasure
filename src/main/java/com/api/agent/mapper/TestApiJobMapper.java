package com.api.agent.mapper;

import com.api.agent.dto.TestApiJobDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestApiJobMapper {
    void insert(TestApiJobDO jobDO);
    void update(TestApiJobDO student);
    void delete(Long id);
    TestApiJobDO selectById(Long id);
    List<TestApiJobDO> selectAll();
}