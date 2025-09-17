package com.skysys.service.controller;

import com.skysys.service.model.R;
import com.skysys.service.model.vo.MigrationResultVO;
import com.skysys.service.service.IMigrationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 迁移管理
 *
 * @author tth
 * @description 迁移管理
 */
@Slf4j
@RestController
@RequestMapping("/migration")
public class MigrationController {

    @Resource
    private IMigrationService migrationService;

    /**
     * 根据站点id集合迁移数据
     *
     * @param siteIds 站点ID
     * @return {@link R }<{@link MigrationResultVO }>
     */
    @PostMapping("/sites")
    public R<MigrationResultVO> migrationSites(@RequestBody List<String> siteIds) {
        return R.ok(migrationService.migrationSites(siteIds));
    }

    /**
     * 根据无人机id集合迁移数据
     *
     * @param uavIds 无人机入侵检测系统
     * @return {@link R }<{@link MigrationResultVO }>
     */
    @PostMapping("/uavs")
    public R<MigrationResultVO> migrationUavs(@RequestBody List<String> uavIds) {
        return R.ok(migrationService.migrationUavs(uavIds));
    }

    /**
     * 根据机库id集合迁移数据
     *
     * @param hiveIds 蜂巢ID
     * @return {@link R }<{@link MigrationResultVO }>
     */
    @PostMapping("/hives")
    public R<MigrationResultVO> migrationHives(@RequestBody List<String> hiveIds) {
        return R.ok(migrationService.migrationHives(hiveIds));
    }

    /**
     * 根据平台ID迁移总线数据
     *
     * @param platId 平台ID
     * @return {@link R }<{@link MigrationResultVO }>
     */
    @PostMapping("/bus")
    public R<MigrationResultVO> migrationBus(@RequestBody Integer platId) {
        return R.ok(migrationService.migrationBus(platId));
    }

}