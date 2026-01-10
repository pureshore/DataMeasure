package com.api.agent.dto.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PlatformCookie {

    private Long id;                     // 主键ID

    private String platformName;        // 平台名称：如 "taobao", "jd", "pdd"

    private String cookieValue;         // Cookie字符串

    private String accountName;         // 账户名称

    private Integer status;             // 状态：1-启用，0-禁用，默认1

    private LocalDateTime expireTime;   // 过期时间

    private LocalDateTime createTime;   // 创建时间

    private LocalDateTime updateTime;   // 更新时间

    private String extraInfo;           // 额外信息（JSON格式）
}
