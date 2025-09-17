package com.skysys.service.service.slave1;

import com.skysys.service.model.entity.slave1.TbDeviceUavs;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author entic
* @description 针对表【tb_device_uavs】的数据库操作Service
* @createDate 2025-09-17 18:44:53
*/
public interface TbDeviceUavsService extends IService<TbDeviceUavs> {

    /**
     * 根据站点ID查询UAV ID列表
     *
     * @param siteId 站点ID
     * @return UAV ID列表
     */
    List<String> selectUavIdsBySiteId(String siteId);

    /**
     * 根据UAV ID列表查询UAV信息列表
     *
     * @param uavIds UAV ID列表
     * @return UAV信息列表
     */
    List<TbDeviceUavs> selectByUavIds(List<String> uavIds);

    /**
     * 根据平台ID查询UAV ID列表
     */
    List<String> selectUavIdsByPlatId(@Param("platId") Integer platId);
}
