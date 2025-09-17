package com.skysys.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.skysys.service.component.convert.HiveConvert;
import com.skysys.service.component.convert.SiteConvert;
import com.skysys.service.component.convert.UavConvert;
import com.skysys.service.model.entity.master.DeviceHive;
import com.skysys.service.model.entity.master.DeviceSite;
import com.skysys.service.model.entity.master.DeviceUav;
import com.skysys.service.model.entity.slave1.TbDeviceHives;
import com.skysys.service.model.entity.slave1.TbDeviceUavs;
import com.skysys.service.model.entity.slave1.TbSites;
import com.skysys.service.model.entity.slave1.TbSysDeviceModels;
import com.skysys.service.model.vo.MigrationResultVO;
import com.skysys.service.service.IMigrationService;
import com.skysys.service.service.master.DeviceHiveService;
import com.skysys.service.service.master.DeviceSiteService;
import com.skysys.service.service.master.DeviceUavService;
import com.skysys.service.service.slave1.TbDeviceHivesService;
import com.skysys.service.service.slave1.TbDeviceUavsService;
import com.skysys.service.service.slave1.TbSitesService;
import com.skysys.service.service.slave1.TbSysDeviceModelsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 数据迁移服务实现类
 *
 * @author: 陶添浩
 * @date: 2025/9/17 15:59:13
 * @description:
 */
@Slf4j
@Service("migrationService")
public class MigrationServiceImpl implements IMigrationService {

    @Autowired
    private TbSitesService tbSitesService;

    @Autowired
    private TbDeviceUavsService tbDeviceUavsService;

    @Autowired
    private TbDeviceHivesService tbDeviceHivesService;

    @Autowired
    private TbSysDeviceModelsService tbSysDeviceModelsService;

    @Autowired
    private DeviceSiteService deviceSiteService;

    @Autowired
    private DeviceUavService deviceUavService;

    @Autowired
    private DeviceHiveService deviceHiveService;

    @Resource
    private SiteConvert siteConvert;

    @Resource
    private UavConvert uavConvert;

    @Resource
    private HiveConvert hiveConvert;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MigrationResultVO migrationSites(List<String> siteIds) {
        // 参数校验
        if (CollectionUtils.isEmpty(siteIds)) {
            log.warn("站点ID列表为空");
            return new MigrationResultVO(false, 0, 0, 0, "站点ID列表不能为空");
        }

        int totalCount = siteIds.size();
        int successCount = 0;
        int failedCount = 0;
        StringBuilder errorMessages = new StringBuilder();

        log.info("开始迁移站点数据，总数量: {}", totalCount);

        try {
            // 1. 批量查询 tbSites 数据
            List<TbSites> tbSitesList = tbSitesService.selectBySiteIds(siteIds);

            if (CollectionUtils.isEmpty(tbSitesList)) {
                log.warn("未找到对应的站点数据, siteIds: {}", siteIds);
                return new MigrationResultVO(false, totalCount, 0, totalCount, "未找到对应的站点数据");
            }

            // 2. 使用 MapStruct 批量转换基础数据
            List<DeviceSite> deviceSites = siteConvert.convert(tbSitesList);

            // 3. 遍历设置关联的 uav_id 和 hive_id，并执行插入操作
            for (int i = 0; i < tbSitesList.size(); i++) {
                TbSites tbSite = tbSitesList.get(i);
                DeviceSite deviceSite = deviceSites.get(i);

                try {
                    // 设置关联的 uav_id 和 hive_id
                    setRelationIds(deviceSite, tbSite.getSiteID());

                    log.debug("正在处理站点: {}", JSON.toJSONString(deviceSite));
                    // 插入到 DeviceSite 表
                    deviceSiteService.save(deviceSite);

                    successCount++;
                    log.debug("成功迁移站点: {}", tbSite.getSiteName());

                } catch (Exception e) {
                    failedCount++;
                    String errorMsg = "迁移站点失败 [" + tbSite.getId() + "]: " + e.getMessage();
                    log.error(errorMsg, e);
                    errorMessages.append(errorMsg).append("; ");
                }
            }

            // 构建结果信息
            String message = String.format("数据迁移完成，总计: %d, 成功: %d, 失败: %d",
                    totalCount, successCount, failedCount);

            if (!errorMessages.isEmpty()) {
                message += " 错误详情: " + errorMessages.toString();
            }

            boolean isSuccess = failedCount == 0;
            log.info("数据迁移完成 - 总计: {}, 成功: {}, 失败: {}", totalCount, successCount, failedCount);

            return new MigrationResultVO(isSuccess, totalCount, successCount, failedCount, message);

        } catch (Exception e) {
            String errorMsg = "数据迁移过程中发生异常: " + e.getMessage();
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MigrationResultVO migrationUavs(List<String> uavIds) {
        // 参数校验
        if (CollectionUtils.isEmpty(uavIds)) {
            log.warn("UAV ID列表为空");
            return new MigrationResultVO(false, 0, 0, 0, "UAV ID列表不能为空");
        }

        int totalCount = uavIds.size();
        int successCount = 0;
        int failedCount = 0;
        StringBuilder errorMessages = new StringBuilder();

        log.info("开始迁移UAV数据，总数量: {}", totalCount);

        try {
            // 1. 批量查询 TbDeviceUavs 数据
            List<TbDeviceUavs> tbDeviceUavsList = tbDeviceUavsService.selectByUavIds(uavIds);

            if (CollectionUtils.isEmpty(tbDeviceUavsList)) {
                log.warn("未找到对应的UAV数据, uavIds: {}", uavIds);
                return new MigrationResultVO(false, totalCount, 0, totalCount, "未找到对应的UAV数据");
            }

            // 2. 遍历处理每个UAV数据
            for (TbDeviceUavs tbDeviceUavs : tbDeviceUavsList) {
                try {
                    // 获取设备型号信息
                    TbSysDeviceModels deviceModel = getDeviceModelById(tbDeviceUavs.getModelID());

                    // 使用MapStruct转换
                    DeviceUav deviceUav = uavConvert.convert(tbDeviceUavs, deviceModel);

                    log.debug("正在处理UAV: {}", JSON.toJSONString(deviceUav));

                    // 插入到 DeviceUav 表
                    deviceUavService.save(deviceUav);

                    successCount++;
                    log.debug("成功迁移UAV: {}", tbDeviceUavs.getUAVName());

                } catch (Exception e) {
                    failedCount++;
                    String errorMsg = "迁移UAV失败 [" + tbDeviceUavs.getUAVID() + "]: " + e.getMessage();
                    log.error(errorMsg, e);
                    errorMessages.append(errorMsg).append("; ");
                }
            }

            // 构建结果信息
            String message = String.format("UAV数据迁移完成，总计: %d, 成功: %d, 失败: %d",
                    totalCount, successCount, failedCount);

            if (!errorMessages.isEmpty()) {
                message += " 错误详情: " + errorMessages.toString();
            }

            boolean isSuccess = failedCount == 0;
            log.info("UAV数据迁移完成 - 总计: {}, 成功: {}, 失败: {}", totalCount, successCount, failedCount);

            return new MigrationResultVO(isSuccess, totalCount, successCount, failedCount, message);

        } catch (Exception e) {
            String errorMsg = "UAV数据迁移过程中发生异常: " + e.getMessage();
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MigrationResultVO migrationHives(List<String> hiveIds) {
        // 参数校验
        if (CollectionUtils.isEmpty(hiveIds)) {
            log.warn("Hive ID列表为空");
            return new MigrationResultVO(false, 0, 0, 0, "Hive ID列表不能为空");
        }

        int totalCount = hiveIds.size();
        int successCount = 0;
        int failedCount = 0;
        StringBuilder errorMessages = new StringBuilder();

        log.info("开始迁移Hive数据，总数量: {}", totalCount);

        try {
            // 1. 批量查询 TbDeviceHives 数据
            List<TbDeviceHives> tbDeviceHivesList = tbDeviceHivesService.selectByHiveIds(hiveIds);

            if (CollectionUtils.isEmpty(tbDeviceHivesList)) {
                log.warn("未找到对应的Hive数据, hiveIds: {}", hiveIds);
                return new MigrationResultVO(false, totalCount, 0, totalCount, "未找到对应的Hive数据");
            }

            // 2. 遍历处理每个Hive数据
            for (TbDeviceHives tbDeviceHives : tbDeviceHivesList) {
                try {
                    // 获取设备型号信息
                    TbSysDeviceModels deviceModel = getDeviceModelById(tbDeviceHives.getModelID());

                    // 使用MapStruct转换
                    DeviceHive deviceHive = hiveConvert.convert(tbDeviceHives, deviceModel);

                    log.debug("正在处理Hive: {}", JSON.toJSONString(deviceHive));

                    // 插入到 DeviceHive 表
                    deviceHiveService.save(deviceHive);

                    successCount++;
                    log.debug("成功迁移Hive: {}", tbDeviceHives.getHiveName());

                } catch (Exception e) {
                    failedCount++;
                    String errorMsg = "迁移Hive失败 [" + tbDeviceHives.getHiveID() + "]: " + e.getMessage();
                    log.error(errorMsg, e);
                    errorMessages.append(errorMsg).append("; ");
                }
            }

            // 构建结果信息
            String message = String.format("Hive数据迁移完成，总计: %d, 成功: %d, 失败: %d",
                    totalCount, successCount, failedCount);

            if (!errorMessages.isEmpty()) {
                message += " 错误详情: " + errorMessages.toString();
            }

            boolean isSuccess = failedCount == 0;
            log.info("Hive数据迁移完成 - 总计: {}, 成功: {}, 失败: {}", totalCount, successCount, failedCount);

            return new MigrationResultVO(isSuccess, totalCount, successCount, failedCount, message);

        } catch (Exception e) {
            String errorMsg = "Hive数据迁移过程中发生异常: " + e.getMessage();
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MigrationResultVO migrationBus(Integer platId) {
        // 参数校验
        if (Objects.isNull(platId)) {
            log.warn("平台ID为空");
            return new MigrationResultVO(false, 0, 0, 0, "平台ID不能为空");
        }

        Integer platformId;
        try {
            platformId = Integer.valueOf(platId);
        } catch (NumberFormatException e) {
            log.error("平台ID格式错误: {}", platId);
            return new MigrationResultVO(false, 0, 0, 0, "平台ID格式错误");
        }

        log.info("开始根据平台ID迁移数据，平台ID: {}", platformId);
        long startTime = System.currentTimeMillis();

        try {
            // 1. 并行查询需要迁移的数据ID列表
            CompletableFuture<List<String>> siteIdsFuture = CompletableFuture.supplyAsync(() -> {
                long siteStart = System.currentTimeMillis();
                List<String> ids = tbSitesService.selectSiteIdsByPlatId(platformId);
                log.debug("查询站点ID耗时: {}ms, 数量: {}", System.currentTimeMillis() - siteStart, ids.size());
                return ids;
            });

            CompletableFuture<List<String>> uavIdsFuture = CompletableFuture.supplyAsync(() -> {
                long uavStart = System.currentTimeMillis();
                List<String> ids = tbDeviceUavsService.selectUavIdsByPlatId(platformId);
                log.debug("查询UAV ID耗时: {}ms, 数量: {}", System.currentTimeMillis() - uavStart, ids.size());
                return ids;
            });

            CompletableFuture<List<String>> hiveIdsFuture = CompletableFuture.supplyAsync(() -> {
                long hiveStart = System.currentTimeMillis();
                List<String> ids = tbDeviceHivesService.selectHiveIdsByPlatId(platformId);
                log.debug("查询机库ID耗时: {}ms, 数量: {}", System.currentTimeMillis() - hiveStart, ids.size());
                return ids;
            });

            // 等待所有查询完成
            CompletableFuture.allOf(siteIdsFuture, uavIdsFuture, hiveIdsFuture).join();

            List<String> siteIds = siteIdsFuture.get();
            List<String> uavIds = uavIdsFuture.get();
            List<String> hiveIds = hiveIdsFuture.get();

            long queryTime = System.currentTimeMillis() - startTime;
            log.info("平台ID: {} 查询完成耗时: {}ms - 站点: {}, UAV: {}, 机库: {}",
                    platformId, queryTime, siteIds.size(), uavIds.size(), hiveIds.size());

            // 2. 顺序执行迁移（保持事务一致性）
            int totalCount = 0;
            int successCount = 0;
            int failedCount = 0;
            StringBuilder resultMessages = new StringBuilder();

            // 迁移站点数据
            if (!CollectionUtils.isEmpty(siteIds)) {
                long migrationStart = System.currentTimeMillis();
                MigrationResultVO siteResult = migrationSites(siteIds);
                totalCount += siteResult.getTotalCount();
                successCount += siteResult.getSuccessCount();
                failedCount += siteResult.getFailedCount();

                long migrationTime = System.currentTimeMillis() - migrationStart;
                log.info("站点迁移完成耗时: {}ms", migrationTime);
                resultMessages.append("站点迁移: ").append(siteResult.getMessage()).append("; ");
            } else {
                resultMessages.append("站点迁移: 未找到需要迁移的数据; ");
            }

            // 迁移UAV数据
            if (!CollectionUtils.isEmpty(uavIds)) {
                long migrationStart = System.currentTimeMillis();
                MigrationResultVO uavResult = migrationUavs(uavIds);
                totalCount += uavResult.getTotalCount();
                successCount += uavResult.getSuccessCount();
                failedCount += uavResult.getFailedCount();

                long migrationTime = System.currentTimeMillis() - migrationStart;
                log.info("UAV迁移完成耗时: {}ms", migrationTime);
                resultMessages.append("UAV迁移: ").append(uavResult.getMessage()).append("; ");
            } else {
                resultMessages.append("UAV迁移: 未找到需要迁移的数据; ");
            }

            // 迁移机库数据
            if (!CollectionUtils.isEmpty(hiveIds)) {
                long migrationStart = System.currentTimeMillis();
                MigrationResultVO hiveResult = migrationHives(hiveIds);
                totalCount += hiveResult.getTotalCount();
                successCount += hiveResult.getSuccessCount();
                failedCount += hiveResult.getFailedCount();

                long migrationTime = System.currentTimeMillis() - migrationStart;
                log.info("机库迁移完成耗时: {}ms", migrationTime);
                resultMessages.append("机库迁移: ").append(hiveResult.getMessage()).append("; ");
            } else {
                resultMessages.append("机库迁移: 未找到需要迁移的数据; ");
            }

            long totalTime = System.currentTimeMillis() - startTime;
            String finalMessage = String.format("平台ID: %s 数据迁移完成，总耗时: %dms，总计: %d, 成功: %d, 失败: %d。详情: %s",
                    platformId, totalTime, totalCount, successCount, failedCount, resultMessages.toString());

            boolean isSuccess = failedCount == 0;
            log.info("平台数据迁移完成 - 平台ID: {}, 总耗时: {}ms, 总计: {}, 成功: {}, 失败: {}",
                    platformId, totalTime, totalCount, successCount, failedCount);

            return new MigrationResultVO(isSuccess, totalCount, successCount, failedCount, finalMessage);

        } catch (Exception e) {
            long totalTime = System.currentTimeMillis() - startTime;
            String errorMsg = "平台数据迁移过程中发生异常，平台ID: " + platformId + ", 耗时: " + totalTime + "ms, 错误: " + e.getMessage();
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }




    // ---------------------------------------- 私有方法 ----------------------------------------

    /**
     * 设置关联的 uav_id 和 hive_id
     */
    private void setRelationIds(DeviceSite deviceSite, String siteId) {
        try {
            // 获取 UAV ID
            List<String> uavIds = getUavIdBySiteId(siteId);
            if (!CollectionUtils.isEmpty(uavIds) && uavIds.size() == 1) {
                deviceSite.setUavId(uavIds.getFirst());
                log.debug("设置站点 {} 的 UAV ID: {}", siteId, uavIds.getFirst());
            }

            // 获取 Hive ID
            String hiveId = getHiveIdBySiteId(siteId);
            if (StringUtils.hasText(hiveId)) {
                deviceSite.setHiveId(hiveId);
                log.debug("设置站点 {} 的 Hive ID: {}", siteId, hiveId);
            }

        } catch (Exception e) {
            log.error("设置关联ID失败, siteId: {}", siteId, e);
        }
    }

    /**
     * 根据 siteId 获取对应的 uav_id
     */
    private List<String> getUavIdBySiteId(String siteId) {
        try {
            List<String> uavIds = tbDeviceUavsService.selectUavIdsBySiteId(siteId);
            log.debug("根据 siteId 获取对应的 uav_ids, siteId: {}, uavIds: {}", siteId, uavIds);
            return uavIds;
        } catch (Exception e) {
            log.error("获取 uav_id 失败, siteId: {}", siteId, e);
            return null;
        }
    }

    /**
     * 根据 siteId 获取对应的 hive_id
     */
    private String getHiveIdBySiteId(String siteId) {
        try {
            return tbDeviceHivesService.selectBySiteId(siteId);
        } catch (Exception e) {
            log.error("获取 hive_id 失败, siteId: {}", siteId, e);
            return null;
        }
    }

    /**
     * 根据modelID获取设备型号信息
     */
    private TbSysDeviceModels getDeviceModelById(Integer modelId) {
        try {
            if (modelId == null) {
                log.warn("modelID为空，返回空对象");
                return new TbSysDeviceModels(); // 返回空对象避免null
            }

            TbSysDeviceModels deviceModel = tbSysDeviceModelsService.getById(modelId);
            if (deviceModel == null) {
                log.warn("未找到对应的设备型号信息, modelId: {}", modelId);
                return new TbSysDeviceModels(); // 返回空对象避免null
            }

            log.debug("获取设备型号信息成功, modelId: {}, name: {}, brand: {}",
                    modelId, deviceModel.getName(), deviceModel.getBrand());
            return deviceModel;

        } catch (Exception e) {
            log.error("获取设备型号信息失败, modelId: {}", modelId, e);
            return new TbSysDeviceModels(); // 返回空对象避免null
        }
    }
}
