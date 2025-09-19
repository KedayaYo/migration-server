package com.skysys.service.service.master;

import com.skysys.service.model.entity.master.DeviceHive;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;

/**
* @author entic
* @description 针对表【device_hive】的数据库操作Service
* @createDate 2025-09-17 14:39:48
*/
public interface DeviceHiveService extends IService<DeviceHive> {

    /**
     * 根据站点ID查询机库ID
     *
     * @param siteId 站点ID
     * @return {@link String }
     */
    String selectHiveIdBySiteId(String siteId);

    /**
     * 根据机库ID查询机库数量
     *
     * @param hiveId 机库ID
     * @return {@link Integer }
     */
    Long countByHiveId(String hiveId);

    /**
     * 查询机库ID集合
     *
     * @param hiveIds 机库ID
     * @return {@link List<DeviceHive> }
     */
    List<String> selectHiveIds(List<String> hiveIds);

    /**
     * 批量插入机库
     *
     * @param successDeviceHives 机库
     */
    boolean insertBatch(List<DeviceHive> successDeviceHives);

    /**
     * 批量更新机库
     *
     * @param conflictHivesToUpdate 机库
     */
    boolean updateBatch(List<DeviceHive> conflictHivesToUpdate);

    /**
     * 根据站点ID集合查询机库
     *
     * @param siteIds 站点ID
     * @return {@link List<DeviceHive> }
     */
    List<DeviceHive> selectBySiteIds(List<String> siteIds);
}
