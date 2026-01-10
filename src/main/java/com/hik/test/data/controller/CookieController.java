package com.hik.test.data.controller;

import com.hik.test.data.dto.entity.PlatformCookie;
import com.hik.test.data.manager.CookieManager;
import com.hik.test.data.service.CookieManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/cookies")
public class CookieController {
    @Autowired
    private CookieManager cookieManager;
    @Autowired
    private CookieManagerService cookieService;

    @GetMapping("/{platform}")
    public String getCookie(@PathVariable String platform) {
        return cookieManager.getCookie(platform);
    }

    /**
     * 获取Cookie（使用Service）
     */
    @GetMapping("/service/{platform}")
    public String getCookieByService(@PathVariable String platform) {
        return cookieService.getCookie(platform);
    }

    /**
     * 更新Cookie
     */
    @PostMapping("/update")
    public String updateCookie(@RequestParam String platform,
                               @RequestParam String cookieValue,
                               @RequestParam(required = false) String accountName) {

        boolean success = cookieManager.updateCookie(
                platform,
                cookieValue,
                accountName,
                LocalDateTime.now().plusDays(7)
        );

        return success ? "更新成功" : "更新失败";
    }

    /**
     * 获取所有Cookie
     */
    @GetMapping("/all")
    public Map<String, PlatformCookie> getAllCookies() {
        return cookieManager.getAllCookies();
    }

    /**
     * 刷新缓存
     */
    @PostMapping("/refresh")
    public String refreshCache() {
        cookieManager.reloadAllCookies();
        return "缓存刷新成功";
    }
}

