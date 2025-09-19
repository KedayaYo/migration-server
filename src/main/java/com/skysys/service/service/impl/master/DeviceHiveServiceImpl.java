package com.skysys.service.service.impl.master;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skysys.service.mapper.master.DeviceHiveMapper;
import com.skysys.service.model.entity.master.DeviceHive;
import com.skysys.service.service.master.DeviceHiveService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author entic
 * @description 针对表【device_hive】的数据库操作Service实现
 * @createDate 2025-09-17 14:39:48
 */
@Service
public class DeviceHiveServiceImpl extends ServiceImpl<DeviceHiveMapper, DeviceHive>
        implements DeviceHiveService {

    @Override
    public String selectHiveIdBySiteId(String siteId) {
        DeviceHive hive = baseMapper.selectOne(new LambdaQueryWrapper<DeviceHive>()
                .eq(DeviceHive::getSiteId, siteId)
                .eq(DeviceHive::getDeleted, 0)
                .last("LIMIT 1")
        );
        return hive != null ? hive.getHiveId() : null;
    }

    @Override
    public Long countByHiveId(String hiveId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<DeviceHive>()
                .eq(DeviceHive::getHiveId, hiveId)
                .eq(DeviceHive::getDeleted, 0));
    }

    @Override
    public List<String> selectHiveIds(List<String> hiveIds) {
        List<String> ids = baseMapper.selectObjs(new LambdaQueryWrapper<DeviceHive>()
                .in(DeviceHive::getHiveId, hiveIds)
                .eq(DeviceHive::getDeleted, 0)
                .select(DeviceHive::getHiveId));
        return new ArrayList<>(new LinkedHashSet<>(ids));
    }

    @Override
    public boolean insertBatch(List<DeviceHive> successDeviceHives) {
        return baseMapper.insertBatch(successDeviceHives);
    }

    @Override
    public boolean updateBatch(List<DeviceHive> conflictHivesToUpdate) {
        return baseMapper.updateBatchById(conflictHivesToUpdate);
    }

    @Override
    public List<DeviceHive> selectBySiteIds(List<String> siteIds) {
        return baseMapper.selectList(new LambdaQueryWrapper<DeviceHive>()
                .in(DeviceHive::getSiteId, siteIds)
                .eq(DeviceHive::getDeleted, 0));
    }
}




