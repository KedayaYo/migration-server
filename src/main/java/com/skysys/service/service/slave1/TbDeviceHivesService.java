package com.skysys.service.service.slave1;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skysys.service.model.entity.slave1.TbDeviceHives;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author entic
 * @description 针对表【tb_device_hives】的数据库操作Service
 * @createDate 2025-09-17 18:44:53
 */
public interface TbDeviceHivesService extends IService<TbDeviceHives> {

    /**
     * 根据站点ID查询机库ID
     *
     * @param siteId 站点ID
     * @return 机库id
     */
    String selectHiveIdBySiteId(String siteId);

    /**
     * 根据机库ID查询设备信息
     *
     * @param hiveIds 机库ID
     * @return 设备信息
     */
    List<TbDeviceHives> selectByHiveIds(List<String> hiveIds);

    /**
     * 根据平台ID查询机库ID列表
     */
    List<String> selectHiveIdsByPlatId(@Param("platId") Integer platId);
    
}
