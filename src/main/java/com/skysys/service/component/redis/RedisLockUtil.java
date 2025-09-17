package com.skysys.service.component.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.Objects;

/**
 * @author: wangrui
 * @date: 2023/12/27 18:18
 * @description: redis锁工具
 */
@Slf4j
public final class RedisLockUtil {

    /**
     * 加锁
     *
     * @param key   redis主键
     * @param value 值
     * @param time  过期时间
     */
    public static boolean lock(String key, String value, long time) {
        final boolean result = Boolean.TRUE.equals(RedisOpsUtils.setIfAbsent(key, value, time));
        if (result) {
            log.info("[redisTemplate redis]设置锁缓存 缓存  url:{} ========缓存时间为{}秒", key, time);
        }
        return result;
    }

    /**
     * 解锁
     *
     * @param key   redis主键
     * @param value 值
     */
    public static boolean unlock(String key, String value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = RedisOpsUtils.execute(redisScript, Collections.singletonList(key), value);
        if (Objects.equals(1L, result)) {
            log.info("[redisTemplate redis]释放锁 缓存  url:{}", key);
            return true;
        }
        return false;
    }
}
