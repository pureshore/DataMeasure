package com.hik.test.data.service;

import com.hik.test.data.dto.JiraDetailDO;

import java.util.List;
import java.util.Map;

public interface JiraDetailService {

    /**
     * 创建JIRA记录
     */
    Long createJira(JiraDetailDO jiraDetail);

    /**
     * 批量创建JIRA记录
     */
    boolean batchCreateJira(List<JiraDetailDO> jiraDetails);

    /**
     * 根据ID获取JIRA详情
     */
    JiraDetailDO getJiraById(Long id);

    /**
     * 根据JIRA单号获取详情
     */
    JiraDetailDO getJiraByNumber(String jiraNumber);

//    /**
//     * 分页查询JIRA列表
//     */
//    PageInfo<JiraDetailDO> queryJiraPage(JiraQueryDTO queryDTO);

    /**
     * 更新JIRA信息
     */
    boolean updateJira(JiraDetailDO jiraDetail);

    /**
     * 删除JIRA记录（逻辑删除）
     */
    boolean deleteJira(Long id);

    /**
     * 批量删除JIRA记录
     */
    boolean batchDeleteJira(List<Long> ids);

    /**
     * 获取各产品线的JIRA统计
     */
//    List<ProductLineCountDTO> getProductLineStatistics();

    /**
     * 获取负责人的JIRA统计
     */
    List<Map<String, Object>> getOwnerStatistics(String owner);

    /**
     * 同步JIRA数据（从外部系统）
     */
    boolean syncJiraData(List<JiraDetailDO> jiraDetails);
}
