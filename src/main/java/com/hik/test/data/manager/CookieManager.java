package com.hik.test.data.manager;


import com.hik.test.data.dto.entity.PlatformCookie;
import com.hik.test.data.service.CookieManagerService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CookieManager {

    // 单例实例
    private static CookieManager instance;

    @Autowired
    private CookieManagerService cookieService;

    // 定时任务执行器
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     * 私有构造方法
     */
    private CookieManager() {}

    /**
     * 获取单例实例
     */
    public static CookieManager getInstance() {
        if (instance == null) {
            synchronized (CookieManager.class) {
                if (instance == null) {
                    log.warn("CookieManager实例尚未初始化，请确保Spring容器已启动");
                }
            }
        }
        return instance;
    }

    /**
     * Spring初始化
     */
    @PostConstruct
    private void init() {
        instance = this;
        startRefreshTask();
        log.info("CookieManager单例初始化完成");
    }

    /**
     * 获取Cookie值
     */
    public String getCookie(String platformName) {
        return cookieService.getCookie(platformName);
    }

    /**
     * 获取Cookie对象
     */
    public PlatformCookie getPlatformCookie(String platformName) {
        return cookieService.getPlatformCookie(platformName);
    }

    /**
     * 更新Cookie
     */
    public boolean updateCookie(String platformName, String cookieValue,
                                String accountName, LocalDateTime expireTime) {
        PlatformCookie cookie = PlatformCookie.builder()
                .platformName(platformName)
                .cookieValue(cookieValue)
                .accountName(accountName)
                .expireTime(expireTime)
                .status(1)
                .build();

        return cookieService.saveOrUpdateCookie(cookie);
    }

    /**
     * 获取所有Cookie
     */
    public Map<String, PlatformCookie> getAllCookies() {
        return cookieService.getAllCookies();
    }

    /**
     * 重新加载所有Cookie
     */
    public void reloadAllCookies() {
        cookieService.loadAllCookies();
    }

    /**
     * 刷新指定平台Cookie
     */
    public void refreshCookie(String platformName) {
        cookieService.refreshCookie(platformName);
    }

    /**
     * 启动定时刷新任务
     */
    private void startRefreshTask() {
        // 每10分钟刷新一次缓存
        scheduler.scheduleAtFixedRate(() -> {
            try {
                log.debug("执行定时刷新Cookie任务");
                cookieService.loadAllCookies();
            } catch (Exception e) {
                log.error("定时刷新Cookie失败", e);
            }
        }, 10, 10, TimeUnit.MINUTES);
    }

    /**
     * 停止管理器
     */
    public void shutdown() {
        scheduler.shutdown();
        cookieService.clearCache();
        log.info("CookieManager已关闭");
    }
}
