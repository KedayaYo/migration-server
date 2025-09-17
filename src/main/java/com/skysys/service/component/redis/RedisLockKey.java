package com.skysys.service.component.redis;

/**
 * @author: wangrui
 * @date: 2024/2/29 17:37
 * @description: Redis分布式锁
 */
public class RedisLockKey {

    public static final String DELIMITER = ":";
    public static final String LOCK_PREFIX = "lock_";

    /**
     * 结束录制
     */
    public static final String STOP_RECORD = LOCK_PREFIX + "stop_record" + DELIMITER;
}
