package com.skysys.service.service.impl;

import com.skysys.service.component.convert.HiveConvert;
import com.skysys.service.component.convert.SiteConvert;
import com.skysys.service.component.convert.UavConvert;
import com.skysys.service.model.bo.ProcessResultBO;
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

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

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
    private Executor threadPoolTaskExecutor;

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
            log.warn("[站点迁移] 站点ID列表为空");
            return new MigrationResultVO(false, 0, 0, 0, "站点ID列表不能为空");
        }

        int totalCount = siteIds.size();
        log.info("[站点迁移] 源数据集合: {}", siteIds);

        try {
            // 1. 批量查询 tbSites 数据
            List<TbSites> tbSitesList = tbSitesService.selectBySiteIds(siteIds);

            if (CollectionUtils.isEmpty(tbSitesList)) {
                log.warn("[站点迁移] 未找到对应的源数据");
                return new MigrationResultVO(false, totalCount, 0, totalCount, "未找到对应的站点数据");
            }

            // 2. 批量查询已存在的 DeviceSite 数据
            List<String> existingSiteIds = deviceSiteService.selectSiteIds(siteIds);
            log.info("[站点迁移] 目标数据重复集合: {}", existingSiteIds);

            // 3. 过滤掉已存在的数据
            List<TbSites> duplicateSites = tbSitesList.stream()
                    .filter(tbSites -> existingSiteIds.contains(tbSites.getSiteID()))
                    .toList();

            tbSitesList.removeIf(tbSites -> existingSiteIds.contains(tbSites.getSiteID()));

            log.info("[站点迁移] 跳过重复数据集合: {}",
                    duplicateSites.stream().map(TbSites::getSiteID).toList());

            if (CollectionUtils.isEmpty(tbSitesList)) {
                log.warn("[站点迁移] 所有站点均已存在，无需迁移");
                return new MigrationResultVO(true, totalCount, 0, 0, "所有站点数据均已存在，无需迁移");
            }

            // 4. 使用线程池并行处理数据转换和关联
            List<CompletableFuture<ProcessResultBO<String, DeviceSite>>> futures = new ArrayList<>();

            for (TbSites tbSites : tbSitesList) {
                CompletableFuture<ProcessResultBO<String, DeviceSite>> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        // 使用MapStruct转换基础数据
                        DeviceSite deviceSite = siteConvert.convert(tbSites);

                        // 设置关联的设备ID
                        String siteId = tbSites.getSiteID();

                        // 查询关联的 UAV
                        List<String> uavIds = tbDeviceUavsService.selectUavIdsBySiteId(siteId);
                        if (!CollectionUtils.isEmpty(uavIds)) {
                            deviceSite.setUavId(uavIds.getFirst());
                        }

                        // 查询关联的 Hive
                        String hiveId = tbDeviceHivesService.selectHiveIdBySiteId(siteId);
                        if (StringUtils.hasText(hiveId)) {
                            deviceSite.setHiveId(hiveId);
                        }

                        return new ProcessResultBO<>(true, tbSites.getSiteID(), deviceSite, null);

                    } catch (Exception e) {
                        String errorMsg = String.format("处理失败: %s", e.getMessage());
                        return new ProcessResultBO<>(false, tbSites.getSiteID(), null, errorMsg);
                    }
                }, threadPoolTaskExecutor);

                futures.add(future);
            }

            // 5. 等待所有任务完成并收集结果
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            List<ProcessResultBO<String, DeviceSite>> processResults = futures.stream()
                    .map(CompletableFuture::join)
                    .toList();

            // 6. 分离成功和失败的数据
            List<DeviceSite> successDeviceSites = processResults.stream()
                    .filter(ProcessResultBO::isSuccess)
                    .map(ProcessResultBO::getTarget)
                    .collect(Collectors.toList());

            List<String> failedSiteIds = processResults.stream()
                    .filter(result -> !result.isSuccess())
                    .map(ProcessResultBO::getSource)
                    .collect(Collectors.toList());

            List<String> failedReasons = processResults.stream()
                    .filter(result -> !result.isSuccess())
                    .map(result -> result.getSource() + ": " + result.getErrorMessage())
                    .collect(Collectors.toList());

            // 7. 批量插入成功处理的数据
            int successCount = 0;
            if (!successDeviceSites.isEmpty()) {
                try {
                    deviceSiteService.insertBatch(successDeviceSites);
                    successCount = successDeviceSites.size();

                    List<String> successSiteIds = successDeviceSites.stream()
                            .map(DeviceSite::getSiteId)
                            .collect(Collectors.toList());
                    log.info("[站点迁移] 成功插入集合: {}", successSiteIds);

                } catch (Exception e) {
                    log.error("[站点迁移] 批量插入失败: {}", e.getMessage());
                    throw new RuntimeException("批量插入站点数据失败: " + e.getMessage(), e);
                }
            }

            // 8. 记录失败信息
            if (!failedSiteIds.isEmpty()) {
                log.warn("[站点迁移] 失败站点ID集合: {}", failedSiteIds);
                log.warn("[站点迁移] 失败原因: {}", failedReasons);
            }

            int failedCount = totalCount - successCount;
            String message = String.format("站点数据迁移完成，总计: %d, 成功: %d, 失败: %d",
                    totalCount, successCount, failedCount);

            boolean isSuccess = failedCount == 0;
            return new MigrationResultVO(isSuccess, totalCount, successCount, failedCount, message);

        } catch (Exception e) {
            String errorMsg = "站点数据迁移过程中发生异常: " + e.getMessage();
            log.error("[站点迁移] {}", errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MigrationResultVO migrationUavs(List<String> uavIds) {
        // 参数校验
        if (CollectionUtils.isEmpty(uavIds)) {
            log.warn("[UAV迁移] UAV ID列表为空");
            return new MigrationResultVO(false, 0, 0, 0, "UAV ID列表不能为空");
        }

        int totalCount = uavIds.size();
        log.info("[UAV迁移] 源数据集合: {}", uavIds);

        try {
            // 1. 批量查询 TbDeviceUavs 数据
            List<TbDeviceUavs> tbDeviceUavsList = tbDeviceUavsService.selectByUavIds(uavIds);

            if (CollectionUtils.isEmpty(tbDeviceUavsList)) {
                log.warn("[UAV迁移] 未找到对应的源数据");
                return new MigrationResultVO(false, totalCount, 0, totalCount, "未找到对应的UAV数据");
            }

            // 2. 批量查询已存在的 DeviceUav 数据
            List<String> existingUavIds = deviceUavService.selectUavIds(uavIds);
            log.info("[UAV迁移] 目标数据重复集合: {}", existingUavIds);

            // 3. 过滤掉已存在的数据
            List<TbDeviceUavs> duplicateUavs = tbDeviceUavsList.stream()
                    .filter(tbDeviceUavs -> existingUavIds.contains(tbDeviceUavs.getUAVID()))
                    .toList();

            tbDeviceUavsList.removeIf(tbDeviceUavs -> existingUavIds.contains(tbDeviceUavs.getUAVID()));

            log.info("[UAV迁移] 跳过重复数据集合: {}",
                    duplicateUavs.stream().map(TbDeviceUavs::getUAVID).toList());

            if (CollectionUtils.isEmpty(tbDeviceUavsList)) {
                log.warn("[UAV迁移] 所有UAV均已存在，无需迁移");
                return new MigrationResultVO(true, totalCount, 0, 0, "所有UAV数据均已存在，无需迁移");
            }

            // 4. 使用线程池并行处理数据转换
            List<CompletableFuture<ProcessResultBO<String, DeviceUav>>> futures = new ArrayList<>();

            for (TbDeviceUavs tbDeviceUavs : tbDeviceUavsList) {
                CompletableFuture<ProcessResultBO<String, DeviceUav>> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        // 获取设备型号信息
                        TbSysDeviceModels deviceModel = getDeviceModelById(tbDeviceUavs.getModelID());

                        // 使用MapStruct转换
                        DeviceUav deviceUav = uavConvert.convert(tbDeviceUavs, deviceModel);

                        return new ProcessResultBO<>(true, tbDeviceUavs.getUAVID(), deviceUav, null);

                    } catch (Exception e) {
                        String errorMsg = String.format("处理失败: %s", e.getMessage());
                        return new ProcessResultBO<>(false, tbDeviceUavs.getUAVID(), null, errorMsg);
                    }
                }, threadPoolTaskExecutor);

                futures.add(future);
            }

            // 5. 等待所有任务完成并收集结果
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            List<ProcessResultBO<String, DeviceUav>> processResults = futures.stream()
                    .map(CompletableFuture::join)
                    .toList();

            // 6. 分离成功和失败的数据
            List<DeviceUav> successDeviceUavs = processResults.stream()
                    .filter(ProcessResultBO::isSuccess)
                    .map(ProcessResultBO::getTarget)
                    .collect(Collectors.toList());

            List<String> failedUavIds = processResults.stream()
                    .filter(result -> !result.isSuccess())
                    .map(ProcessResultBO::getSource)
                    .collect(Collectors.toList());

            List<String> failedReasons = processResults.stream()
                    .filter(result -> !result.isSuccess())
                    .map(result -> result.getSource() + ": " + result.getErrorMessage())
                    .collect(Collectors.toList());

            // 7. 批量插入所有成功处理的数据，然后处理重复站点的无人机冲突
            int successCount = 0;
            if (!successDeviceUavs.isEmpty()) {
                try {
                    // 7.1 先批量插入所有数据
                    deviceUavService.insertBatch(successDeviceUavs);
                    successCount = successDeviceUavs.size();

                    List<String> insertedUavIds = successDeviceUavs.stream()
                            .map(DeviceUav::getUavId)
                            .collect(Collectors.toList());
                    log.info("[UAV迁移] 批量插入完成，插入数量: {}, 插入UAV ID: {}", successCount, insertedUavIds);

                    // 7.2 检查重复站点的无人机冲突 - 查询数据库中所有相关UAV
                    List<String> involvedSiteIds = successDeviceUavs.stream()
                            .filter(uav -> StringUtils.hasText(uav.getSiteId()))
                            .map(DeviceUav::getSiteId)
                            .distinct()
                            .collect(Collectors.toList());

                    if (!involvedSiteIds.isEmpty()) {
                        // 查询数据库中绑定到这些站点的所有UAV（包括本次插入的和之前存在的）
                        List<DeviceUav> allSiteUavs = deviceUavService.selectBySiteIds(involvedSiteIds);

                        Map<String, List<DeviceUav>> siteUavMap = allSiteUavs.stream()
                                .filter(uav -> StringUtils.hasText(uav.getSiteId()))
                                .collect(Collectors.groupingBy(DeviceUav::getSiteId));

                        // 找出有多个无人机绑定同一站点的情况
                        List<String> conflictSiteIds = siteUavMap.entrySet().stream()
                                .filter(entry -> entry.getValue().size() > 1)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList());

                        if (!conflictSiteIds.isEmpty()) {
                            log.warn("[UAV迁移] 发现重复站点绑定冲突，站点ID: {}", conflictSiteIds);

                            // 查询站点表中实际绑定的无人机ID
                            List<DeviceSite> conflictSites = deviceSiteService.selectBySiteIds(conflictSiteIds);
                            Map<String, String> siteUavBindingMap = conflictSites.stream()
                                    .filter(site -> StringUtils.hasText(site.getUavId()))
                                    .collect(Collectors.toMap(DeviceSite::getSiteId, DeviceSite::getUavId));

                            // 找出需要标记删除的冲突无人机
                            List<DeviceUav> conflictUavsToUpdate = new ArrayList<>();
                            for (String siteId : conflictSiteIds) {
                                String boundUavId = siteUavBindingMap.get(siteId);
                                List<DeviceUav> conflictUavs = siteUavMap.get(siteId);

                                for (DeviceUav uav : conflictUavs) {
                                    // 不是站点绑定的无人机且未被删除就标记为删除
                                    if (!uav.getUavId().equals(boundUavId) && uav.getDeleted() != 1) {
                                        uav.setDeleted(1);
                                        conflictUavsToUpdate.add(uav);
                                    }
                                }
                            }

                            // 7.3 批量更新冲突的无人机删除状态
                            if (!conflictUavsToUpdate.isEmpty()) {
                                deviceUavService.updateBatch(conflictUavsToUpdate);

                                List<String> conflictUavIds = conflictUavsToUpdate.stream()
                                        .map(DeviceUav::getUavId)
                                        .collect(Collectors.toList());
                                log.info("[UAV迁移] 标记冲突无人机为删除状态，数量: {}, UAV ID: {}",
                                        conflictUavsToUpdate.size(), conflictUavIds);
                            }
                        }
                    }

                } catch (Exception e) {
                    log.error("[UAV迁移] 批量插入或冲突处理失败: {}", e.getMessage(), e);
                    throw new RuntimeException("批量插入UAV数据失败: " + e.getMessage(), e);
                }
            }

            // 8. 记录失败信息
            if (!failedUavIds.isEmpty()) {
                log.warn("[UAV迁移] 失败UAVId集合: {}", failedUavIds);
                log.warn("[UAV迁移] 失败原因: {}", failedReasons);
            }

            int failedCount = totalCount - successCount;
            String message = String.format("UAV数据迁移完成，总计: %d, 成功: %d, 失败: %d",
                    totalCount, successCount, failedCount);

            boolean isSuccess = failedCount == 0;

            return new MigrationResultVO(isSuccess, totalCount, successCount, failedCount, message);

        } catch (Exception e) {
            String errorMsg = "UAV数据迁移过程中发生异常: " + e.getMessage();
            log.error("[UAV迁移] {}", errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MigrationResultVO migrationHives(List<String> hiveIds) {
        // 参数校验
        if (CollectionUtils.isEmpty(hiveIds)) {
            log.warn("[机库迁移] Hive ID列表为空");
            return new MigrationResultVO(false, 0, 0, 0, "Hive ID列表不能为空");
        }

        int totalCount = hiveIds.size();
        log.info("[机库迁移] 源数据集合: {}", hiveIds);

        try {
            // 1. 批量查询 TbDeviceHives 数据
            List<TbDeviceHives> tbDeviceHivesList = tbDeviceHivesService.selectByHiveIds(hiveIds);

            if (CollectionUtils.isEmpty(tbDeviceHivesList)) {
                log.warn("[机库迁移] 未找到对应的源数据");
                return new MigrationResultVO(false, totalCount, 0, totalCount, "未找到对应的Hive数据");
            }

            // 2. 批量查询已存在的 DeviceHive 数据
            List<String> existingHiveIds = deviceHiveService.selectHiveIds(hiveIds);
            log.info("[机库迁移] 目标数据重复集合: {}", existingHiveIds);

            // 3. 过滤掉已存在的数据
            List<TbDeviceHives> duplicateHives = tbDeviceHivesList.stream()
                    .filter(tbDeviceHives -> existingHiveIds.contains(tbDeviceHives.getHiveID()))
                    .toList();

            tbDeviceHivesList.removeIf(tbDeviceHives -> existingHiveIds.contains(tbDeviceHives.getHiveID()));

            log.info("[机库迁移] 跳过重复数据集合: {}",
                    duplicateHives.stream().map(TbDeviceHives::getHiveID).toList());

            if (CollectionUtils.isEmpty(tbDeviceHivesList)) {
                log.warn("[机库迁移] 所有Hive均已存在，无需迁移");
                return new MigrationResultVO(true, totalCount, 0, 0, "所有Hive数据均已存在，无需迁移");
            }

            // 4. 使用线程池并行处理数据转换
            List<CompletableFuture<ProcessResultBO<String, DeviceHive>>> futures = new ArrayList<>();

            for (TbDeviceHives tbDeviceHives : tbDeviceHivesList) {
                CompletableFuture<ProcessResultBO<String, DeviceHive>> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        // 获取设备型号信息
                        TbSysDeviceModels deviceModel = getDeviceModelById(tbDeviceHives.getModelID());

                        // 使用MapStruct转换
                        DeviceHive deviceHive = hiveConvert.convert(tbDeviceHives, deviceModel);

                        return new ProcessResultBO<>(true, tbDeviceHives.getHiveID(), deviceHive, null);

                    } catch (Exception e) {
                        String errorMsg = String.format("处理失败: %s", e.getMessage());
                        return new ProcessResultBO<>(false, tbDeviceHives.getHiveID(), null, errorMsg);
                    }
                }, threadPoolTaskExecutor);

                futures.add(future);
            }

            // 5. 等待所有任务完成并收集结果
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            List<ProcessResultBO<String, DeviceHive>> processResults = futures.stream()
                    .map(CompletableFuture::join)
                    .toList();

            // 6. 分离成功和失败的数据
            List<DeviceHive> successDeviceHives = processResults.stream()
                    .filter(ProcessResultBO::isSuccess)
                    .map(ProcessResultBO::getTarget)
                    .collect(Collectors.toList());

            List<String> failedHiveIds = processResults.stream()
                    .filter(result -> !result.isSuccess())
                    .map(ProcessResultBO::getSource)
                    .collect(Collectors.toList());

            List<String> failedReasons = processResults.stream()
                    .filter(result -> !result.isSuccess())
                    .map(result -> result.getSource() + ": " + result.getErrorMessage())
                    .collect(Collectors.toList());

            // 7. 批量插入所有成功处理的数据，然后处理重复站点的机库冲突
            int successCount = 0;
            if (!successDeviceHives.isEmpty()) {
                try {
                    // 7.1 先批量插入所有数据
                    deviceHiveService.insertBatch(successDeviceHives);
                    successCount = successDeviceHives.size();

                    List<String> insertedHiveIds = successDeviceHives.stream()
                            .map(DeviceHive::getHiveId)
                            .collect(Collectors.toList());
                    log.info("[机库迁移] 批量插入完成，插入数量: {}, 插入Hive ID: {}", successCount, insertedHiveIds);

                    // 7.2 检查重复站点的机库冲突
                    Map<String, List<DeviceHive>> siteHiveMap = successDeviceHives.stream()
                            .filter(hive -> StringUtils.hasText(hive.getSiteId()))
                            .collect(Collectors.groupingBy(DeviceHive::getSiteId));

                    // 获取涉及的站点ID
                    Set<String> involvedSiteIds = siteHiveMap.keySet();

                    if (!involvedSiteIds.isEmpty()) {
                        log.info("[机库迁移] 检查站点机库冲突，涉及站点: {}", involvedSiteIds);

                        // 查询这些站点在数据库中的所有机库（包括刚插入的和之前存在的）
                        List<DeviceHive> allSiteHives = deviceHiveService.selectBySiteIds(new ArrayList<>(involvedSiteIds));

                        // 按站点分组所有机库
                        Map<String, List<DeviceHive>> allSiteHiveMap = allSiteHives.stream()
                                .collect(Collectors.groupingBy(DeviceHive::getSiteId));

                        // 找出有多个机库绑定同一站点的情况
                        List<String> conflictSiteIds = allSiteHiveMap.entrySet().stream()
                                .filter(entry -> entry.getValue().size() > 1)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList());

                        if (!conflictSiteIds.isEmpty()) {
                            log.warn("[机库迁移] 发现重复站点绑定冲突，站点ID: {}", conflictSiteIds);

                            // 查询站点表中实际绑定的机库ID
                            List<DeviceSite> conflictSites = deviceSiteService.selectBySiteIds(conflictSiteIds);
                            Map<String, String> siteHiveBindingMap = conflictSites.stream()
                                    .filter(site -> StringUtils.hasText(site.getHiveId()))
                                    .collect(Collectors.toMap(DeviceSite::getSiteId, DeviceSite::getHiveId));

                            // 找出需要标记删除的冲突机库
                            List<DeviceHive> conflictHivesToUpdate = new ArrayList<>();
                            for (String siteId : conflictSiteIds) {
                                String boundHiveId = siteHiveBindingMap.get(siteId);
                                List<DeviceHive> allConflictHives = allSiteHiveMap.get(siteId);

                                for (DeviceHive hive : allConflictHives) {
                                    // 不是站点绑定的机库且未被删除的就标记为删除
                                    if (!hive.getHiveId().equals(boundHiveId)) {
                                        hive.setDeleted("1");
                                        conflictHivesToUpdate.add(hive);
                                    }
                                }
                            }

                            // 7.3 批量更新冲突的机库删除状态
                            if (!conflictHivesToUpdate.isEmpty()) {
                                deviceHiveService.updateBatch(conflictHivesToUpdate);

                                List<String> conflictHiveIds = conflictHivesToUpdate.stream()
                                        .map(DeviceHive::getHiveId)
                                        .collect(Collectors.toList());
                                log.info("[机库迁移] 标记冲突机库为删除状态，数量: {}, Hive ID: {}",
                                        conflictHivesToUpdate.size(), conflictHiveIds);
                            }
                        }
                    }

                } catch (Exception e) {
                    log.error("[机库迁移] 批量插入或冲突处理失败: {}", e.getMessage(), e);
                    throw new RuntimeException("批量插入Hive数据失败: " + e.getMessage(), e);
                }
            }

            // 8. 记录失败信息
            if (!failedHiveIds.isEmpty()) {
                log.warn("[机库迁移] 失败HiveId集合: {}", failedHiveIds);
                log.warn("[机库迁移] 失败原因: {}", failedReasons);
            }

            int failedCount = totalCount - successCount;
            String message = String.format("Hive数据迁移完成，总计: %d, 成功: %d, 失败: %d",
                    totalCount, successCount, failedCount);

            boolean isSuccess = failedCount == 0;
            return new MigrationResultVO(isSuccess, totalCount, successCount, failedCount, message);

        } catch (Exception e) {
            String errorMsg = "Hive数据迁移过程中发生异常: " + e.getMessage();
            log.error("[机库迁移] {}", errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MigrationResultVO migrationBus(Integer platId) {
        // 参数校验
        if (Objects.isNull(platId)) {
            log.warn("[总线迁移] 参数校验失败，平台ID为空");
            return new MigrationResultVO(false, 0, 0, 0, "平台ID不能为空");
        }

        log.info("[总线迁移] 开始迁移，平台ID: {}", platId);
        long startTime = System.currentTimeMillis();

        try {
            // 1. 并行查询需要迁移的数据ID列表
            log.info("[总线迁移] 开始并行查询相关数据，平台ID: {}", platId);

            CompletableFuture<List<String>> siteIdsFuture = CompletableFuture.supplyAsync(() -> {
                long siteStart = System.currentTimeMillis();
                List<String> ids = tbSitesService.selectSiteIdsByPlatId(platId);
                log.debug("[总线迁移] 查询站点ID完成，耗时: {}ms, 数量: {}, ID列表: {}",
                        System.currentTimeMillis() - siteStart, ids.size(), ids);
                return ids;
            });

            CompletableFuture<List<String>> uavIdsFuture = CompletableFuture.supplyAsync(() -> {
                long uavStart = System.currentTimeMillis();
                List<String> ids = tbDeviceUavsService.selectUavIdsByPlatId(platId);
                log.debug("[总线迁移] 查询UAV ID完成，耗时: {}ms, 数量: {}, ID列表: {}",
                        System.currentTimeMillis() - uavStart, ids.size(), ids);
                return ids;
            });

            CompletableFuture<List<String>> hiveIdsFuture = CompletableFuture.supplyAsync(() -> {
                long hiveStart = System.currentTimeMillis();
                List<String> ids = tbDeviceHivesService.selectHiveIdsByPlatId(platId);
                log.debug("[总线迁移] 查询机库ID完成，耗时: {}ms, 数量: {}, ID列表: {}",
                        System.currentTimeMillis() - hiveStart, ids.size(), ids);
                return ids;
            });

            // 等待所有查询完成
            CompletableFuture.allOf(siteIdsFuture, uavIdsFuture, hiveIdsFuture).join();

            List<String> siteIds = siteIdsFuture.get();
            List<String> uavIds = uavIdsFuture.get();
            List<String> hiveIds = hiveIdsFuture.get();

            long queryTime = System.currentTimeMillis() - startTime;
            log.info("[总线迁移] 并行查询完成，平台ID: {}, 耗时: {}ms, 站点: {} 个, UAV: {} 个, 机库: {} 个",
                    platId, queryTime, siteIds.size(), uavIds.size(), hiveIds.size());

            log.info("[总线迁移] 查询结果详情，平台ID: {}, 站点ID: {}, UAV ID: {}, 机库ID: {}",
                    platId, siteIds, uavIds, hiveIds);

            // 2. 顺序执行迁移（保持事务一致性）
            int totalCount = 0;
            int successCount = 0;
            int failedCount = 0;
            StringBuilder resultMessages = new StringBuilder();

            log.info("[总线迁移] 开始顺序执行数据迁移，平台ID: {}", platId);

            // 迁移站点数据
            if (!CollectionUtils.isEmpty(siteIds)) {
                log.info("[总线迁移] 开始迁移站点数据，平台ID: {}, 站点数量: {}", platId, siteIds.size());
                long migrationStart = System.currentTimeMillis();
                MigrationResultVO siteResult = migrationSites(siteIds);
                totalCount += siteResult.getTotalCount();
                successCount += siteResult.getSuccessCount();
                failedCount += siteResult.getFailedCount();
                long migrationTime = System.currentTimeMillis() - migrationStart;
                log.info("[总线迁移] 站点迁移完成，平台ID: {}, 耗时: {}ms, 成功: {}, 失败: {}",
                        platId, migrationTime, siteResult.getSuccessCount(), siteResult.getFailedCount());
                resultMessages.append("站点迁移: ").append(siteResult.getMessage()).append("; ");
            } else {
                log.info("[总线迁移] 跳过站点迁移，平台ID: {}, 原因: 未找到需要迁移的站点数据", platId);
                resultMessages.append("站点迁移: 未找到需要迁移的数据; ");
            }

            // 迁移UAV数据
            if (!CollectionUtils.isEmpty(uavIds)) {
                log.info("[总线迁移] 开始迁移UAV数据，平台ID: {}, UAV数量: {}", platId, uavIds.size());
                long migrationStart = System.currentTimeMillis();
                MigrationResultVO uavResult = migrationUavs(uavIds);
                totalCount += uavResult.getTotalCount();
                successCount += uavResult.getSuccessCount();
                failedCount += uavResult.getFailedCount();
                long migrationTime = System.currentTimeMillis() - migrationStart;
                log.info("[总线迁移] UAV迁移完成，平台ID: {}, 耗时: {}ms, 成功: {}, 失败: {}",
                        platId, migrationTime, uavResult.getSuccessCount(), uavResult.getFailedCount());
                resultMessages.append("UAV迁移: ").append(uavResult.getMessage()).append("; ");
            } else {
                log.info("[总线迁移] 跳过UAV迁移，平台ID: {}, 原因: 未找到需要迁移的UAV数据", platId);
                resultMessages.append("UAV迁移: 未找到需要迁移的数据; ");
            }

            // 迁移机库数据
            if (!CollectionUtils.isEmpty(hiveIds)) {
                log.info("[总线迁移] 开始迁移机库数据，平台ID: {}, 机库数量: {}", platId, hiveIds.size());
                long migrationStart = System.currentTimeMillis();
                MigrationResultVO hiveResult = migrationHives(hiveIds);
                totalCount += hiveResult.getTotalCount();
                successCount += hiveResult.getSuccessCount();
                failedCount += hiveResult.getFailedCount();
                long migrationTime = System.currentTimeMillis() - migrationStart;
                log.info("[总线迁移] 机库迁移完成，平台ID: {}, 耗时: {}ms, 成功: {}, 失败: {}",
                        platId, migrationTime, hiveResult.getSuccessCount(), hiveResult.getFailedCount());
                resultMessages.append("机库迁移: ").append(hiveResult.getMessage()).append("; ");
            } else {
                log.info("[总线迁移] 跳过机库迁移，平台ID: {}, 原因: 未找到需要迁移的机库数据", platId);
                resultMessages.append("机库迁移: 未找到需要迁移的数据; ");
            }

            long totalTime = System.currentTimeMillis() - startTime;
            double successRate = totalCount > 0 ? (successCount * 100.0 / totalCount) : 0;

            String finalMessage = String.format("平台ID: %s 数据迁移完成，总耗时: %dms，总计: %d, 成功: %d, 失败: %d。详情: %s",
                    platId, totalTime, totalCount, successCount, failedCount, resultMessages.toString());

            boolean isSuccess = failedCount == 0;

            log.info("[总线迁移] 迁移完成，平台ID: {}, 总耗时: {}ms, 总计: {}, 成功: {}, 失败: {}, 成功率: {}%",
                    platId, totalTime, totalCount, successCount, failedCount, successRate);

            return new MigrationResultVO(isSuccess, totalCount, successCount, failedCount, finalMessage);

        } catch (Exception e) {
            long totalTime = System.currentTimeMillis() - startTime;
            String errorMsg = "平台数据迁移过程中发生异常，平台ID: " + platId + ", 耗时: " + totalTime + "ms, 错误: " + e.getMessage();
            log.error("[总线迁移] 迁移异常，平台ID: {}, 总耗时: {}ms, 错误信息: {}",
                    platId, totalTime, e.getMessage(), e);
            throw new RuntimeException(errorMsg, e);
        }
    }


    // ---------------------------------------- 私有方法 ----------------------------------------

    /**
     * 根据设备型号ID获取设备型号信息
     */
    private TbSysDeviceModels getDeviceModelById(Integer modelId) {
        if (Objects.isNull(modelId)) {
            log.warn("[设备型号查询] 型号ID为空");
            throw new RuntimeException("设备型号ID不能为空");
        }

        log.debug("[设备型号查询] 查询设备型号，型号ID: {}", modelId);
        TbSysDeviceModels deviceModel = tbSysDeviceModelsService.getById(modelId);

        if (Objects.isNull(deviceModel)) {
            log.error("[设备型号查询] 未找到设备型号信息，型号ID: {}", modelId);
            throw new RuntimeException("未找到设备型号信息，型号ID: " + modelId);
        }

        log.debug("[设备型号查询] 查询成功，型号ID: {}, 型号名称: {}, 品牌: {}",
                modelId, deviceModel.getModelCode(), deviceModel.getBrand());
        return deviceModel;
    }
}

