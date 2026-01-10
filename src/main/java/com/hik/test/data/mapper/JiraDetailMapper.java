package com.hik.test.data.mapper;

import com.hik.test.data.dto.JiraDetailDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface JiraDetailMapper {

    int insert(JiraDetailDO record);

    int batchInsert(List<JiraDetailDO> list);

    JiraDetailDO selectById(Long id);

    JiraDetailDO selectByJiraNumber(String jiraNumber);

//    List<JiraDetailDO> selectPage(JiraQueryDTO queryDTO);

    int update(JiraDetailDO record);

    int deleteById(Long id);

    int batchDelete(List<Long> ids);

//    List<ProductLineCountDTO> countByProductLine();
    List<Map<String, Object>> countByOwner(@Param("owner") String owner);
}
