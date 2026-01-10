package com.api.agent.task;

import com.api.agent.service.CookieManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CookieCleanupTask {
    @Autowired
    private CookieManagerService cookieService;

    /**
     * 每天凌晨2点清理过期Cookie
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredCookies() {
        log.info("开始执行过期Cookie清理任务...");
        int cleanedCount = cookieService.cleanupExpiredCookies();
        log.info("过期Cookie清理完成，共清理{}个Cookie", cleanedCount);
    }

    /**
     * 每小时检查一次即将过期的Cookie
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void checkExpiringCookies() {
        // 可以发送通知或执行其他逻辑
        log.debug("检查即将过期的Cookie...");
    }
}
