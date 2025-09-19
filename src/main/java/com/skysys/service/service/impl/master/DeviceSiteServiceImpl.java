package com.skysys.service.service.impl.master;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skysys.service.mapper.master.DeviceSiteMapper;
import com.skysys.service.model.entity.master.DeviceSite;
import com.skysys.service.service.master.DeviceSiteService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author entic
 * @description 针对表【device_site】的数据库操作Service实现
 * @createDate 2025-09-17 14:39:48
 */
@Service
public class DeviceSiteServiceImpl extends ServiceImpl<DeviceSiteMapper, DeviceSite>
        implements DeviceSiteService {

    @Override
    public List<DeviceSite> selectBySiteIds(List<String> siteIds) {
        return baseMapper.selectList(new LambdaQueryWrapper<DeviceSite>()
                .in(DeviceSite::getSiteId, siteIds)
                .eq(DeviceSite::getDeleted, 0));
    }

    @Override
    public List<String> selectSiteIds(List<String> siteIds) {
        return baseMapper.selectList(new LambdaQueryWrapper<DeviceSite>()
                .in(DeviceSite::getSiteId, siteIds)
                .eq(DeviceSite::getDeleted, 0))
                .stream()
                .map(DeviceSite::getSiteId)
                .toList();
    }

    @Override
    public boolean insertBatch(List<DeviceSite> successDeviceSites) {
        return baseMapper.insertBatch(successDeviceSites);
    }
}




