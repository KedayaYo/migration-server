package com.skysys.service.service.impl.master;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skysys.service.mapper.master.DeviceUavMapper;
import com.skysys.service.model.entity.master.DeviceUav;
import com.skysys.service.service.master.DeviceUavService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author entic
 * @description 针对表【device_uav】的数据库操作Service实现
 * @createDate 2025-09-17 14:39:48
 */
@Service
public class DeviceUavServiceImpl extends ServiceImpl<DeviceUavMapper, DeviceUav>
        implements DeviceUavService {

    @Override
    public List<String> selectUavIdsBySiteId(String siteId) {
        List<String> uavIds = baseMapper.selectObjs(new LambdaQueryWrapper<DeviceUav>()
                .eq(DeviceUav::getSiteId, siteId)
                .eq(DeviceUav::getDeleted, 0)
                .select(DeviceUav::getUavId));
        return new ArrayList<>(new LinkedHashSet<>(uavIds));
    }

    @Override
    public List<String> selectUavIds(List<String> uavIds) {
        List<String> ids = baseMapper.selectObjs(new LambdaQueryWrapper<DeviceUav>()
                .in(DeviceUav::getUavId, uavIds)
                .eq(DeviceUav::getDeleted, 0)
                .select(DeviceUav::getUavId));
        return new ArrayList<>(new LinkedHashSet<>(ids));
    }

    @Override
    public boolean insertBatch(List<DeviceUav> successDeviceUavs) {
        return baseMapper.insertBatch(successDeviceUavs);
    }

    @Override
    public boolean updateBatch(List<DeviceUav> deviceUavs) {
        return baseMapper.updateBatchById(deviceUavs);
    }

    @Override
    public List<DeviceUav> selectBySiteIds(List<String> involvedSiteIds) {
        return baseMapper.selectList(new LambdaQueryWrapper<DeviceUav>()
                .in(DeviceUav::getSiteId, involvedSiteIds)
                .eq(DeviceUav::getDeleted, 0));
    }

}




