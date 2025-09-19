package com.skysys.service.service.impl.slave1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skysys.service.mapper.slave1.TbDeviceHivesMapper;
import com.skysys.service.mapper.slave1.TbSysHivePlatformsMapper;
import com.skysys.service.model.entity.slave1.TbDeviceHives;
import com.skysys.service.model.entity.slave1.TbSysHivePlatforms;
import com.skysys.service.service.slave1.TbDeviceHivesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author entic
 * @description 针对表【tb_device_hives】的数据库操作Service实现
 * @createDate 2025-09-17 18:44:53
 */
@Service
public class TbDeviceHivesServiceImpl extends ServiceImpl<TbDeviceHivesMapper, TbDeviceHives>
        implements TbDeviceHivesService {

    @Autowired
    private TbSysHivePlatformsMapper sysHivePlatformsMapper;

    @Override
    public String selectHiveIdBySiteId(String siteId) {
        TbDeviceHives hive = baseMapper.selectOne(
                new LambdaQueryWrapper<TbDeviceHives>()
                        .eq(TbDeviceHives::getSiteID, siteId)
                        .eq(TbDeviceHives::getIsDelete, 0)
                        .last("LIMIT 1")
        );
        return hive != null ? hive.getHiveID() : null;
    }

    @Override
    public List<TbDeviceHives> selectByHiveIds(List<String> hiveIds) {
        if (CollectionUtils.isEmpty(hiveIds)) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<TbDeviceHives> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(TbDeviceHives::getHiveID, hiveIds);
        wrapper.eq(TbDeviceHives::getIsDelete, 0);

        return list(wrapper);
    }

    @Override
    public List<String> selectHiveIdsByPlatId(Integer platId) {
        return baseMapper.selectHiveIdsByPlatId(platId);
    }

}




