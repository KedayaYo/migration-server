package com.skysys.service.service.master;

import com.skysys.service.model.entity.master.DeviceSite;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author entic
* @description 针对表【device_site】的数据库操作Service
* @createDate 2025-09-17 14:39:48
*/
public interface DeviceSiteService extends IService<DeviceSite> {

    /**
     * 根据站点ID集合查询站点信息
     *
     * @param siteIds 站点ID
     * @return {@link List }<{@link DeviceSite }>
     */
    List<DeviceSite> selectBySiteIds(List<String> siteIds);

    /**
     * 根据站点ID集合查询站点ID
     *
     * @param siteIds 站点ID
     * @return {@link List }<{@link String }>
     */
    List<String> selectSiteIds(List<String> siteIds);

    /**
     * 批量插入站点信息
     *
     * @param successDeviceSites 站点信息
     * @return
     */
    boolean insertBatch(List<DeviceSite> successDeviceSites);

}
