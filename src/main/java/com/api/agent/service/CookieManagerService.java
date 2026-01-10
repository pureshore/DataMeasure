package com.api.agent.service;

import com.api.agent.dto.entity.PlatformCookie;
import com.api.agent.mapper.PlatformCookieMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class CookieManagerService {

    // Cookie缓存
    private final Map<String, PlatformCookie> cookieCache = new ConcurrentHashMap<>();
    @Autowired
    private PlatformCookieMapper cookieMapper;

    /**
     * 初始化加载Cookie
     */
    @PostConstruct
    public void init() {
        loadAllCookies();
        log.info("CookieManagerService初始化完成，加载了 {} 个Cookie", cookieCache.size());
    }

    /**
     * 从数据库加载所有启用的Cookie到缓存
     */
    public void loadAllCookies() {
        try {
            // 查询所有启用的Cookie
            List<PlatformCookie> cookies = cookieMapper.selectByStatus(1);

            // 清空缓存
            cookieCache.clear();

            // 加载到缓存
            cookies.forEach(cookie -> {
                cookieCache.put(cookie.getPlatformName().toLowerCase(), cookie);
                log.debug("加载Cookie: {} -> {}", cookie.getPlatformName(), cookie.getAccountName());
            });

            log.info("成功加载 {} 个Cookie到缓存", cookieCache.size());
        } catch (Exception e) {
            log.error("加载Cookie数据失败", e);
        }
    }

    /**
     * 获取指定平台的Cookie
     */
    public String getCookie(String platformName) {
        PlatformCookie cookie = getPlatformCookie(platformName);
        return cookie != null ? cookie.getCookieValue() : null;
    }

    /**
     * 获取完整的Cookie对象
     */
    public PlatformCookie getPlatformCookie(String platformName) {
        PlatformCookie cookie = cookieCache.get(platformName.toLowerCase());
        if (cookie != null && isValid(cookie)) {
            return cookie;
        }
        return null;
    }

    /**
     * 检查Cookie是否有效
     */
    private boolean isValid(PlatformCookie cookie) {
        if (cookie.getStatus() != 1) {
            return false;
        }

        if (cookie.getExpireTime() != null &&
                cookie.getExpireTime().isBefore(LocalDateTime.now())) {
            log.warn("Cookie已过期: {} - {}",
                    cookie.getPlatformName(), cookie.getAccountName());
            // 自动更新状态为过期
            updateCookieStatus(cookie.getId(), 0);
            return false;
        }

        return cookie.getCookieValue() != null && !cookie.getCookieValue().trim().isEmpty();
    }

    /**
     * 保存或更新Cookie
     */
    @Transactional
    public boolean saveOrUpdateCookie(PlatformCookie cookie) {
        try {
            // 设置时间
            if (cookie.getId() == null) {
                cookie.setCreateTime(LocalDateTime.now());
            }
            cookie.setUpdateTime(LocalDateTime.now());

            // 查询是否已存在
            PlatformCookie existing = cookieMapper.selectByPlatformName(cookie.getPlatformName());

            int result;
            if (existing != null) {
                // 更新
                cookie.setId(existing.getId());
                result = cookieMapper.updateById(cookie);
            } else {
                // 新增
                result = cookieMapper.insert(cookie);
            }

            if (result > 0) {
                // 更新缓存
                cookieCache.put(cookie.getPlatformName().toLowerCase(), cookie);
                log.info("保存Cookie成功: {}", cookie.getPlatformName());
                return true;
            }
        } catch (Exception e) {
            log.error("保存Cookie失败: {}", cookie.getPlatformName(), e);
        }
        return false;
    }

    /**
     * 更新Cookie状态
     */
    @Transactional
    public boolean updateCookieStatus(Long id, Integer status) {
        try {
            PlatformCookie cookie = cookieMapper.selectById(id);
            if (cookie != null) {
                cookie.setStatus(status);
                cookie.setUpdateTime(LocalDateTime.now());
                int result = cookieMapper.updateById(cookie);

                if (result > 0) {
                    // 更新缓存
                    if (status == 1) {
                        cookieCache.put(cookie.getPlatformName().toLowerCase(), cookie);
                    } else {
                        cookieCache.remove(cookie.getPlatformName().toLowerCase());
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("更新Cookie状态失败: id={}, status={}", id, status, e);
        }
        return false;
    }

    /**
     * 批量更新Cookie状态
     */
    @Transactional
    public int batchUpdateStatus(List<Long> ids, Integer status) {
        try {
            int result = cookieMapper.batchUpdateStatus(ids, status);
            if (result > 0) {
                // 重新加载缓存
                loadAllCookies();
                log.info("批量更新{}个Cookie状态为{}", result, status);
            }
            return result;
        } catch (Exception e) {
            log.error("批量更新Cookie状态失败", e);
            return 0;
        }
    }

    /**
     * 删除Cookie
     */
    @Transactional
    public boolean deleteCookie(String platformName) {
        try {
            int result = cookieMapper.deleteByPlatformName(platformName);
            if (result > 0) {
                cookieCache.remove(platformName.toLowerCase());
                log.info("删除Cookie成功: {}", platformName);
                return true;
            }
        } catch (Exception e) {
            log.error("删除Cookie失败: {}", platformName, e);
        }
        return false;
    }

    /**
     * 获取所有Cookie
     */
    public Map<String, PlatformCookie> getAllCookies() {
        return new ConcurrentHashMap<>(cookieCache);
    }

    /**
     * 获取即将过期的Cookie
     */
    public List<PlatformCookie> getExpiringCookies() {
        LocalDateTime threshold = LocalDateTime.now().plusDays(1); // 一天内过期
        return cookieMapper.selectExpiringCookies(threshold);
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        cookieCache.clear();
        log.info("Cookie缓存已清空");
    }

    /**
     * 刷新指定平台Cookie
     */
    public void refreshCookie(String platformName) {
        try {
            PlatformCookie cookie = cookieMapper.selectByPlatformNameAndStatus(
                    platformName, 1);
            if (cookie != null) {
                cookieCache.put(platformName.toLowerCase(), cookie);
                log.info("刷新Cookie: {}", platformName);
            } else {
                cookieCache.remove(platformName.toLowerCase());
            }
        } catch (Exception e) {
            log.error("刷新Cookie失败: {}", platformName, e);
        }
    }

    /**
     * 批量更新Cookie状态（增强版）
     */
    @Transactional
    public int batchUpdateCookieStatus(List<Long> ids, Integer status, String platformName) {
        try {
            int result = cookieMapper.batchUpdateStatus(ids, status);

            if (result > 0) {
                // 重新加载缓存
                loadAllCookies();
                log.info("批量更新了{}个Cookie的状态为{}", result, status);
            }
            return result;
        } catch (Exception e) {
            log.error("批量更新Cookie状态失败", e);
            return 0;
        }
    }

    /**
     * 清理过期Cookie
     */
    @Transactional
    public int cleanupExpiredCookies() {
        try {
            // 查找已过期的Cookie
            List<PlatformCookie> expiredCookies = cookieMapper.selectByStatusAndExpireTime(
                    1, LocalDateTime.now());

            if (!expiredCookies.isEmpty()) {
                List<Long> expiredIds = expiredCookies.stream()
                        .map(PlatformCookie::getId)
                        .collect(java.util.stream.Collectors.toList());

                // 批量更新为禁用状态
                int result = batchUpdateCookieStatus(expiredIds, 0, null);

                log.info("清理了{}个过期Cookie", result);
                return result;
            }
        } catch (Exception e) {
            log.error("清理过期Cookie失败", e);
        }
        return 0;
    }
}