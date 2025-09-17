package com.skysys.service.service;

import com.skysys.service.model.vo.MigrationResultVO;

import java.util.List;

/**
 * @author: 陶添浩
 * @date: 2025/9/17 15:59:13
 * @description:
 */
public interface IMigrationService {

    /**
     * 迁移站点
     *
     * @param siteIds 站点id
     * @return 迁移结果
     */
    MigrationResultVO migrationSites(List<String> siteIds);

    /**
     * 迁移无人机
     *
     * @param uavIds 无人机入侵检测系统
     * @return {@link MigrationResultVO }
     */
    MigrationResultVO migrationUavs(List<String> uavIds);

    /**
     * 根据机库id集合迁移数据
     *
     * @param hiveIds 蜂巢ID
     * @return {@link MigrationResultVO }
     */
    MigrationResultVO migrationHives(List<String> hiveIds);

    /**
     * 根据平台ID迁移总线数据
     *
     * @param platId 平台ID
     * @return {@link MigrationResultVO }
     */
    MigrationResultVO migrationBus(Integer platId);
}
