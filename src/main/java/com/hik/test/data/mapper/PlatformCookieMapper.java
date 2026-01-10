package com.hik.test.data.mapper;

import com.hik.test.data.dto.entity.PlatformCookie;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
@Repository
public interface PlatformCookieMapper {

    /**
     * 插入Cookie记录
     */
    @Insert({
            "INSERT INTO platform_cookies(",
            "  platform_name, cookie_value, account_name, status,",
            "  expire_time, create_time, update_time, extra_info",
            ") VALUES(",
            "  #{platformName}, #{cookieValue}, #{accountName}, #{status},",
            "  #{expireTime}, #{createTime}, #{updateTime}, #{extraInfo}",
            ")"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PlatformCookie cookie);

    /**
     * 根据ID更新Cookie
     */
    @Update({
            "<script>",
            "UPDATE platform_cookies",
            "<set>",
            "  <if test='platformName != null'>platform_name = #{platformName},</if>",
            "  <if test='cookieValue != null'>cookie_value = #{cookieValue},</if>",
            "  <if test='accountName != null'>account_name = #{accountName},</if>",
            "  <if test='status != null'>status = #{status},</if>",
            "  <if test='expireTime != null'>expire_time = #{expireTime},</if>",
            "  <if test='updateTime != null'>update_time = #{updateTime},</if>",
            "  <if test='extraInfo != null'>extra_info = #{extraInfo},</if>",
            "</set>",
            "WHERE id = #{id}",
            "</script>"
    })
    int updateById(PlatformCookie cookie);

    /**
     * 根据平台名称更新Cookie
     */
    @Update({
            "UPDATE platform_cookies SET",
            "  cookie_value = #{cookieValue},",
            "  account_name = #{accountName},",
            "  status = #{status},",
            "  expire_time = #{expireTime},",
            "  update_time = #{updateTime},",
            "  extra_info = #{extraInfo}",
            "WHERE platform_name = #{platformName}"
    })
    int updateByPlatformName(PlatformCookie cookie);

    /**
     * 批量更新状态
     * @param ids ID列表
     * @param status 状态
     * @return 更新记录数
     */
    @Update({
            "<script>",
            "UPDATE platform_cookies",
            "SET status = #{status},",
            "    update_time = NOW()",
            "WHERE id IN",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "   #{id}",
            "</foreach>",
            "</script>"
    })
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 根据状态和过期时间查询
     */
    @Select("SELECT * FROM platform_cookies WHERE status = #{status} AND expire_time < #{expireTime}")
    List<PlatformCookie> selectByStatusAndExpireTime(@Param("status") Integer status,
                                                     @Param("expireTime") LocalDateTime expireTime);

    /**
     * 更新过期时间
     */
    @Update("UPDATE platform_cookies SET expire_time = #{expireTime}, update_time = NOW() WHERE id = #{id}")
    int updateExpireTime(@Param("id") Long id, @Param("expireTime") LocalDateTime expireTime);

    /**
     * 批量更新过期时间
     */
    @Update({
            "<script>",
            "UPDATE platform_cookies",
            "SET expire_time = #{expireTime},",
            "    update_time = NOW()",
            "WHERE id IN",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "   #{id}",
            "</foreach>",
            "</script>"
    })
    int batchUpdateExpireTime(@Param("ids") List<Long> ids, @Param("expireTime") LocalDateTime expireTime);

    /**
     * 根据ID删除
     */
    @Delete("DELETE FROM platform_cookies WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 根据平台名称删除
     */
    @Delete("DELETE FROM platform_cookies WHERE platform_name = #{platformName}")
    int deleteByPlatformName(String platformName);

    /**
     * 根据ID查询
     */
    @Select("SELECT * FROM platform_cookies WHERE id = #{id}")
    PlatformCookie selectById(Long id);

    /**
     * 根据平台名称查询
     */
    @Select("SELECT * FROM platform_cookies WHERE platform_name = #{platformName}")
    PlatformCookie selectByPlatformName(String platformName);

    /**
     * 根据状态查询
     */
    @Select("SELECT * FROM platform_cookies WHERE status = #{status}")
    List<PlatformCookie> selectByStatus(Integer status);

    /**
     * 根据平台名称和状态查询
     */
    @Select("SELECT * FROM platform_cookies WHERE platform_name = #{platformName} AND status = #{status}")
    PlatformCookie selectByPlatformNameAndStatus(@Param("platformName") String platformName,
                                                 @Param("status") Integer status);

    /**
     * 查询所有Cookie
     */
    @Select("SELECT * FROM platform_cookies")
    List<PlatformCookie> selectAll();

    /**
     * 查询即将过期的Cookie
     */
    @Select("SELECT * FROM platform_cookies WHERE status = 1 AND expire_time < #{threshold}")
    List<PlatformCookie> selectExpiringCookies(LocalDateTime threshold);

    /**
     * 统计平台数量
     */
    @Select("SELECT COUNT(*) FROM platform_cookies WHERE status = 1")
    int countActiveCookies();

    /**
     * 批量插入（使用XML配置）
     */
    int batchInsert(List<PlatformCookie> cookies);
}
