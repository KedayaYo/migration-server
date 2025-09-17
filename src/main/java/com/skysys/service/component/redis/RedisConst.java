package com.skysys.service.component.redis;

/**
 * @author: wangrui
 * @date: 2024/1/29 17:37
 * @description: Redis key
 */
public final class RedisConst {

    /**
     * 分隔符
     */
    public static final String DELIMITER = ":";

    /**
     * 系统用户信息
     */
    public static final String SYS_USER_INFO = "sys_user_info" + DELIMITER;

    /**
     * 无人机报文
     */
    public static final String DRONE_OSD = "drone_osd" + DELIMITER;

    /**
     * 机库报文
     */
    public static final String DOCK_OSD = "dock_osd" + DELIMITER;

    /**
     * 无人车报文
     */
    public static final String UGV_OSD = "ugv_osd" + DELIMITER;

    /**
     * 无人车业务报文
     */
    public static final String UGV_BUSINESS_OSD = "ugv_business_osd" + DELIMITER;

    /**
     * 设备信息
     */
    public static final String DEVICE_INFO = "device_info";

    /**
     * 设备信息
     */
    public static final String DEVICE_MODEL_INFO = "device_model_info" + DELIMITER;

    /**
     * 网联无人机MSDK报文
     */
    public static final String DRONE_MSDK_OSD = "drone_msdk_osd" + DELIMITER;

    /**
     * 无人机流程事件
     */
    public static final String DRONE_EVENT = "drone_event" + DELIMITER;

    /**
     * 无人机任务中原始报文
     */
    public static final String FLIGHT_RECORD_ORIGINAL = "flight_record_original" + DELIMITER;

    /**
     * 无人机任务信息
     */
    public static final String FLIGHT_RECORD = "flight_record" + DELIMITER;

    /**
     * 无人机正在飞行任务
     */
    public static final String ON_FLIGHT = "on_flight" + DELIMITER;

    /**
     * 任务流程监控
     */
    public static final String MISSION_PROCESS = "mission_process" + DELIMITER;

    /**
     * 任务下发参数缓存
     */
    public static final String MISSION_START = "mission_start" + DELIMITER;

    /**
     * 用户拥有的无人机集合
     */
    public static final String USER_ONLINE_UAV = "user_online_uav" + DELIMITER;

    /**
     * 任务录制
     */
    public static final String MISSION_RECORD = "mission_record" + DELIMITER;

    /**
     * 无人机离线，但是飞行轨迹还在，存储的是第一次为空的时间，与当前时间比较，相差30秒没上报，代表离线
     */
    public static final String DRONE_OFFLINE_BUT_HAS_FLIGHT_RECORDS = "drone_offline_but_has_flight_records" + DELIMITER;

    /**
     * 无人机空闲，但是飞行轨迹还在,与当前时间比较，5秒后仍上报空闲状态，代表任务异常结束
     */
    public static final String DRONE_IDLE_BUT_HAS_FLIGHT_RECORDS = "drone_idle_but_has_flight_records" + DELIMITER;

    /**
     * SRS推流鉴权key
     */
    public static final String SRS_PUSH_STREAM_AUTH_KEY = "srs_push_stream_auth_key";

    /**
     * 摇杆
     */
    public static final String JOYSTICK = "joystick" + DELIMITER;

    /**
     * 开始|结束录频
     */
    public static final String ON_RECORD_BEGIN = "on_record_begin" + DELIMITER;

    /**
     * 停止录频
     */
    public static final String STOP_RECORD = "stop_record" + DELIMITER;

    /**
     * 转码
     */
    public static final String LIVE_TRANSCODE = "live_transcode" + DELIMITER;

    /**
     * 获取任务录制key
     *
     * @param uavId 无人机id
     */
    public static String getRecordKey(String uavId) {
        return MISSION_RECORD + uavId;
    }

    /**
     * 专门用来删除 redis key, 方便定位
     *
     * @param uavId 无人机序列号
     */
    public static void deleteRecordKey(String uavId) {
        String recordKey = getRecordKey(uavId);
        RedisOpsUtils.del(recordKey);
    }

}
