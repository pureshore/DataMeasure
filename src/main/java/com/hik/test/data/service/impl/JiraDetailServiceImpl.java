package com.hik.test.data.service.impl;

import com.hik.test.data.dto.JiraDetailDO;
import com.hik.test.data.mapper.JiraDetailMapper;
import com.hik.test.data.service.JiraDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JiraDetailServiceImpl implements JiraDetailService {
    private final JiraDetailMapper jiraDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createJira(JiraDetailDO jiraDetail) {
        try {
            // 检查JIRA单号是否已存在
            JiraDetailDO existing = jiraDetailMapper.selectByJiraNumber(jiraDetail.getJiraNumber());
            if (existing != null) {
                throw new RuntimeException("JIRA单号已存在: " + jiraDetail.getJiraNumber());
            }

            // 设置默认值
            if (jiraDetail.getStatus() == null) {
                jiraDetail.setStatus(1);
            }

            jiraDetailMapper.insert(jiraDetail);
            log.info("创建JIRA记录成功, ID: {}, JIRA单号: {}",
                    jiraDetail.getId(), jiraDetail.getJiraNumber());
            return jiraDetail.getId();
        } catch (Exception e) {
            log.error("创建JIRA记录失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建JIRA记录失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateJira(List<JiraDetailDO> jiraDetails) {
        if (CollectionUtils.isEmpty(jiraDetails)) {
            return false;
        }

        try {
            // 过滤掉已存在的JIRA单号
            List<String> jiraNumbers = jiraDetails.stream()
                    .map(JiraDetailDO::getJiraNumber)
                    .collect(Collectors.toList());

            Set<String> existingNumbers = jiraDetails.stream()
                    .filter(jira -> jiraDetailMapper.selectByJiraNumber(jira.getJiraNumber()) != null)
                    .map(JiraDetailDO::getJiraNumber)
                    .collect(Collectors.toSet());

            List<JiraDetailDO> newJiras = jiraDetails.stream()
                    .filter(jira -> !existingNumbers.contains(jira.getJiraNumber()))
                    .peek(jira -> {
                        if (jira.getStatus() == null) {
                            jira.setStatus(1);
                        }
                    })
                    .collect(Collectors.toList());

            if (!newJiras.isEmpty()) {
                jiraDetailMapper.batchInsert(newJiras);
                log.info("批量创建JIRA记录成功, 数量: {}", newJiras.size());
            }

            if (!existingNumbers.isEmpty()) {
                log.warn("以下JIRA单号已存在, 跳过创建: {}", existingNumbers);
            }

            return true;
        } catch (Exception e) {
            log.error("批量创建JIRA记录失败: {}", e.getMessage(), e);
            throw new RuntimeException("批量创建JIRA记录失败", e);
        }
    }

    @Override
    public JiraDetailDO getJiraById(Long id) {
        return jiraDetailMapper.selectById(id);
    }

    @Override
    public JiraDetailDO getJiraByNumber(String jiraNumber) {
        return jiraDetailMapper.selectByJiraNumber(jiraNumber);
    }

//    @Override
//    public PageInfo<JiraDetail> queryJiraPage(JiraQueryDTO queryDTO) {
//        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
//        List<JiraDetail> list = jiraDetailMapper.selectPage(queryDTO);
//        return new PageInfo<>(list);
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateJira(JiraDetailDO jiraDetail) {
        try {
            if (jiraDetail.getId() == null) {
                throw new RuntimeException("更新JIRA记录失败: ID不能为空");
            }

            // 如果修改了JIRA单号，检查是否已存在
            if (jiraDetail.getJiraNumber() != null) {
                JiraDetailDO existing = jiraDetailMapper.selectByJiraNumber(jiraDetail.getJiraNumber());
                if (existing != null && !existing.getId().equals(jiraDetail.getId())) {
                    throw new RuntimeException("JIRA单号已存在: " + jiraDetail.getJiraNumber());
                }
            }

            int result = jiraDetailMapper.update(jiraDetail);
            if (result > 0) {
                log.info("更新JIRA记录成功, ID: {}", jiraDetail.getId());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("更新JIRA记录失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新JIRA记录失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteJira(Long id) {
        try {
            int result = jiraDetailMapper.deleteById(id);
            if (result > 0) {
                log.info("删除JIRA记录成功, ID: {}", id);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("删除JIRA记录失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除JIRA记录失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteJira(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }

        try {
            int result = jiraDetailMapper.batchDelete(ids);
            log.info("批量删除JIRA记录成功, 数量: {}", result);
            return result > 0;
        } catch (Exception e) {
            log.error("批量删除JIRA记录失败: {}", e.getMessage(), e);
            throw new RuntimeException("批量删除JIRA记录失败", e);
        }
    }

//    @Override
//    public List<ProductLineCountDTO> getProductLineStatistics() {
//        return jiraDetailMapper.countByProductLine();
//    }

    @Override
    public List<Map<String, Object>> getOwnerStatistics(String owner) {
        return jiraDetailMapper.countByOwner(owner);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncJiraData(List<JiraDetailDO> jiraDetails) {
        if (CollectionUtils.isEmpty(jiraDetails)) {
            return false;
        }

        try {
            log.info("开始同步JIRA数据, 总数: {}", jiraDetails.size());

            // 按JIRA单号分组
            Map<String, JiraDetailDO> syncDataMap = jiraDetails.stream()
                    .collect(Collectors.toMap(
                            JiraDetailDO::getJiraNumber,
                            jira -> jira,
                            (existing, replacement) -> replacement));

            // 查询已存在的JIRA记录
            List<String> existingNumbers = new ArrayList<>();
            for (String jiraNumber : syncDataMap.keySet()) {
                JiraDetailDO existing = jiraDetailMapper.selectByJiraNumber(jiraNumber);
                if (existing != null) {
                    existingNumbers.add(jiraNumber);
                }
            }

            // 分离新增和更新的数据
            List<JiraDetailDO> toInsert = new ArrayList<>();
            List<JiraDetailDO> toUpdate = new ArrayList<>();

            for (Map.Entry<String, JiraDetailDO> entry : syncDataMap.entrySet()) {
                JiraDetailDO jiraDetail = entry.getValue();
                jiraDetail.setStatus(1); // 确保状态正常

                if (existingNumbers.contains(entry.getKey())) {
                    toUpdate.add(jiraDetail);
                } else {
                    toInsert.add(jiraDetail);
                }
            }

            // 批量插入新增数据
            if (!toInsert.isEmpty()) {
                jiraDetailMapper.batchInsert(toInsert);
                log.info("同步新增JIRA数据成功, 数量: {}", toInsert.size());
            }

            // 批量更新已有数据
            int updateCount = 0;
            for (JiraDetailDO jiraDetail : toUpdate) {
                JiraDetailDO existing = jiraDetailMapper.selectByJiraNumber(jiraDetail.getJiraNumber());
                if (existing != null) {
                    jiraDetail.setId(existing.getId());
                    jiraDetailMapper.update(jiraDetail);
                    updateCount++;
                }
            }

            if (!toUpdate.isEmpty()) {
                log.info("同步更新JIRA数据成功, 数量: {}", updateCount);
            }

            log.info("JIRA数据同步完成, 新增: {}, 更新: {}", toInsert.size(), updateCount);
            return true;

        } catch (Exception e) {
            log.error("同步JIRA数据失败: {}", e.getMessage(), e);
            throw new RuntimeException("同步JIRA数据失败", e);
        }
    }
}
