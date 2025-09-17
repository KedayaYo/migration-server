package com.skysys.service.component.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author sean
 * @version 1.0
 * @date 2022/4/19
 */
@Slf4j
@Component
public class RedisOpsUtils {

    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisOpsUtils.redisTemplate = redisTemplate;
    }

    /**
     * HSET
     */
    public static void hashSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * HSETALL
     */
    public static void hashSetAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * HGET
     */
    public static Object hashGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public static Map<Object, Object> hashGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HKEYS
     */
    public static Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * HEXISTS
     */
    public static boolean hashCheck(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * HDEL
     */
    public static boolean hashDel(String key, Object[] fields) {
        return redisTemplate.opsForHash().delete(key, fields) > 0;
    }

    /**
     * HLEN
     */
    public static long hashLen(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * EXPIRE
     */
    public static boolean expireKey(String key, long timeout) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, TimeUnit.SECONDS));
    }

    /**
     * SET
     */
    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * GET
     */
    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * SETEX
     */
    public static void setWithExpire(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * TTL
     */
    public static long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * EXISTS
     */
    public static boolean checkExist(String key) {
        return redisTemplate.hasKey(key);
    }

    public static Set<String> getPatternKey(String pattern) {
        return redisTemplate.keys(pattern.concat("*"));
    }

    /**
     * DEL
     */
    public static boolean del(String key) {
        return RedisOpsUtils.checkExist(key) && redisTemplate.delete(key);
    }

    /**
     * KEYS
     */
    public static Set<String> getAllKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * RPUSH
     */
    public static void listRPush(String key, Object... value) {
        for (Object val : value) {
            redisTemplate.opsForList().rightPush(key, val);
        }
    }

    /**
     * 删除list里面指定的元素
     *
     * @param key
     * @param value
     */
    public static void removeElementFromList(String key, Object... value) {
        for (Object val : value) {
            redisTemplate.opsForList().remove(key, 1, val);
        }
    }

    /**
     * LRANGE
     */
    public static List<Object> listGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * LRANGE
     */
    public static List<Object> listGetAll(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * LLen
     */
    public static Long listLen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * Lindex
     *
     * @return
     */
    public static Object listIndexGet(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * ZADD
     */
    public static Boolean zAdd(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * ZREM
     */
    public static Boolean zRemove(String key, Object... value) {
        return redisTemplate.opsForZSet().remove(key, value) > 0;
    }

    /**
     * ZRANGE
     */
    public static Set<Object> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * ZRANGE
     */
    public static Object zGetMin(String key) {
        Set<Object> objects = zRange(key, 0, 0);
        if (CollectionUtils.isEmpty(objects)) {
            return null;
        }
        return objects.iterator().next();
    }

    /**
     * ZSCORE
     */
    public static Double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * ZINCRBY
     */
    public static Double zIncrement(String key, Object value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * execute
     *
     * @param redisScript
     * @param list
     * @param args
     * @return
     */
    public static Long execute(RedisScript<Long> redisScript, List<String> list, Object... args) {
        return redisTemplate.execute(redisScript, list, args);
    }

    /**
     * setIfAbsent
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public static Boolean setIfAbsent(String key, Object value, long expire) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, expire, TimeUnit.SECONDS);
    }
}
