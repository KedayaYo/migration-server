package com.skysys.service.service.master;

import com.skysys.service.model.entity.master.DeviceUav;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author entic
* @description 针对表【device_uav】的数据库操作Service
* @createDate 2025-09-17 14:39:48
*/
public interface DeviceUavService extends IService<DeviceUav> {

    /**
     * 根据站点ID查询UAVID集合
     *
     * @param siteId 站点ID
     * @return {@link List }<{@link String }>
     */
    List<String> selectUavIdsBySiteId(String siteId);

    /**
     * 查询无人机ID集合
     *
     * @param uavIds 无人机入侵检测系统
     * @return {@link List }<{@link String }>
     */
    List<String> selectUavIds(List<String> uavIds);

    /**
     * 批量插入
     *
     * @param successDeviceUavs 成功的无人机
     */
    boolean insertBatch(List<DeviceUav> successDeviceUavs);

    /**
     * 批量更新
     *
     * @param deviceUavs 无人机
     * @return
     */
    boolean updateBatch(List<DeviceUav> deviceUavs);

    /**
     * 根据站点ID集合查询无人机
     *
     * @param involvedSiteIds 站点ID集合
     * @return {@link List }<{@link DeviceUav }>
     */
    List<DeviceUav> selectBySiteIds(List<String> involvedSiteIds);
}
