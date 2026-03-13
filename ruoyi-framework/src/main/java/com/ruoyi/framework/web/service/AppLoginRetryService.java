package com.ruoyi.framework.web.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.user.AppLoginLockedException;

@Component
public class AppLoginRetryService
{
    @Autowired
    private RedisCache redisCache;

    private static final int MAX_RETRY_COUNT = 5;

    private String getCacheKey(String username)
    {
        return CacheConstants.APP_LOGIN_ERR_CNT_KEY + username;
    }

    public void checkLocked(String username)
    {
        Integer retryCount = redisCache.getCacheObject(getCacheKey(username));
        if (retryCount != null && retryCount >= MAX_RETRY_COUNT)
        {
            throw new AppLoginLockedException();
        }
    }

    public void recordLoginFail(String username)
    {
        String key = getCacheKey(username);
        Integer retryCount = redisCache.getCacheObject(key);
        if (retryCount == null)
        {
            retryCount = 0;
        }
        retryCount = retryCount + 1;
        long secondsUntilMidnight = getSecondsUntilMidnight();
        redisCache.setCacheObject(key, retryCount, (int) secondsUntilMidnight, TimeUnit.SECONDS);
    }

    public void clearLoginFailCount(String username)
    {
        String key = getCacheKey(username);
        if (redisCache.hasKey(key))
        {
            redisCache.deleteObject(key);
        }
    }

    private long getSecondsUntilMidnight()
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT);
        return Duration.between(now, midnight).getSeconds();
    }
}
