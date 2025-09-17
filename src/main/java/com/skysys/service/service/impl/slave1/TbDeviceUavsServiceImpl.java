package com.skysys.service.service.impl.slave1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skysys.service.mapper.slave1.TbDeviceUavsMapper;
import com.skysys.service.model.entity.slave1.TbDeviceUavs;
import com.skysys.service.service.slave1.TbDeviceUavsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author entic
 * @description 针对表【tb_device_uavs】的数据库操作Service实现
 * @createDate 2025-09-17 18:44:53
 */
@Service
public class TbDeviceUavsServiceImpl extends ServiceImpl<TbDeviceUavsMapper, TbDeviceUavs>
        implements TbDeviceUavsService {

    @Override
    public List<String> selectUavIdsBySiteId(String siteId) {
        List<String> uavIds = baseMapper.selectObjs(
                new LambdaQueryWrapper<TbDeviceUavs>()
                        .eq(TbDeviceUavs::getSiteID, siteId)
                        .eq(TbDeviceUavs::getIsDelete, 0)
                        .select(TbDeviceUavs::getUAVID)
        );

        return new ArrayList<>(new LinkedHashSet<>(uavIds));
    }

    @Override
    public List<TbDeviceUavs> selectByUavIds(List<String> uavIds) {
        return baseMapper.selectList(new LambdaQueryWrapper<TbDeviceUavs>()
                .in(TbDeviceUavs::getUAVID, uavIds)
                .eq(TbDeviceUavs::getIsDelete, 0));
    }

    @Override
    public List<String> selectUavIdsByPlatId(Integer platId) {
        return baseMapper.selectUavIdsByPlatId(platId);
    }

}




