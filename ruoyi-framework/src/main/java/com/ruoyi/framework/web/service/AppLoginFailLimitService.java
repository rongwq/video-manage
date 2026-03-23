package com.ruoyi.framework.web.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.user.LoginFailLimitExceededException;

/**
 * APP登录失败次数限制服务
 * 以用户账号 + 当天为维度，统计连续登录失败次数
 *
 * @author ruoyi
 */
@Component
public class AppLoginFailLimitService
{
    @Autowired
    private RedisCache redisCache;

    @Value(value = "${app.login.maxFailCount:5}")
    private int maxFailCount;

    /**
     * 获取登录失败次数缓存键名
     * 格式: app_login_fail_cnt:{username}:{yyyyMMdd}
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username)
    {
        String today = LocalDate.now().toString().replace("-", "");
        return CacheConstants.APP_LOGIN_FAIL_CNT_KEY + username + ":" + today;
    }

    /**
     * 获取锁定的缓存键名（用于标记账号已被锁定）
     * 格式: app_login_fail_cnt:{username}:locked
     *
     * @param username 用户名
     * @return 锁定缓存键key
     */
    private String getLockCacheKey(String username)
    {
        String today = LocalDate.now().toString().replace("-", "");
        return CacheConstants.APP_LOGIN_FAIL_CNT_KEY + username + ":" + today + ":locked";
    }

    /**
     * 检查账号是否被锁定
     *
     * @param username 用户名
     * @return true-已被锁定，false-未被锁定
     */
    public boolean isLocked(String username)
    {
        String lockKey = getLockCacheKey(username);
        return redisCache.hasKey(lockKey);
    }

    /**
     * 验证登录失败次数限制
     * 如果超过限制，抛出异常
     *
     * @param username 用户名
     */
    public void validate(String username)
    {
        // 先检查是否已被锁定
        if (isLocked(username))
        {
            throw new LoginFailLimitExceededException();
        }

        String cacheKey = getCacheKey(username);
        Integer failCount = redisCache.getCacheObject(cacheKey);

        if (failCount == null)
        {
            failCount = 0;
        }

        // 如果失败次数已达到上限，锁定账号
        if (failCount >= maxFailCount)
        {
            lockAccount(username);
            throw new LoginFailLimitExceededException();
        }
    }

    /**
     * 记录登录失败次数
     *
     * @param username 用户名
     */
    public void recordFail(String username)
    {
        String cacheKey = getCacheKey(username);
        Integer failCount = redisCache.getCacheObject(cacheKey);

        if (failCount == null)
        {
            failCount = 0;
        }

        failCount = failCount + 1;

        // 计算到当天23:59:59的剩余秒数
        long expireSeconds = getExpireSeconds();

        // 设置缓存，过期时间为当天剩余时间
        redisCache.setCacheObject(cacheKey, failCount, (int) expireSeconds, TimeUnit.SECONDS);

        // 如果失败次数达到上限，锁定账号
        if (failCount >= maxFailCount)
        {
            lockAccount(username);
        }
    }

    /**
     * 锁定账号（直至次日0点自动解除）
     *
     * @param username 用户名
     */
    private void lockAccount(String username)
    {
        String lockKey = getLockCacheKey(username);
        long expireSeconds = getExpireSeconds();
        redisCache.setCacheObject(lockKey, true, (int) expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 登录成功时清空失败次数
     *
     * @param username 用户名
     */
    public void clearFailRecord(String username)
    {
        String cacheKey = getCacheKey(username);
        String lockKey = getLockCacheKey(username);

        if (redisCache.hasKey(cacheKey))
        {
            redisCache.deleteObject(cacheKey);
        }

        if (redisCache.hasKey(lockKey))
        {
            redisCache.deleteObject(lockKey);
        }
    }

    /**
     * 获取当前到当天23:59:59的剩余秒数
     *
     * @return 剩余秒数
     */
    private long getExpireSeconds()
    {
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plus(1, ChronoUnit.DAYS);
        // 计算从当前时间到明天0点的秒数
        return java.time.Duration.between(
                java.time.LocalDateTime.now(),
                tomorrow.atStartOfDay()
        ).getSeconds();
    }

    /**
     * 获取当前失败次数
     *
     * @param username 用户名
     * @return 失败次数
     */
    public int getFailCount(String username)
    {
        String cacheKey = getCacheKey(username);
        Integer failCount = redisCache.getCacheObject(cacheKey);
        return failCount == null ? 0 : failCount;
    }
}
