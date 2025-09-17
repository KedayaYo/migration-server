package com.skysys.service.service.impl.slave1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skysys.service.mapper.slave1.TbSysSitePlatformsMapper;
import com.skysys.service.model.entity.slave1.TbSites;
import com.skysys.service.model.entity.slave1.TbSysSitePlatforms;
import com.skysys.service.service.slave1.TbSitesService;
import com.skysys.service.mapper.slave1.TbSitesMapper;
import com.skysys.service.service.slave1.TbSysSitePlatformsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author entic
* @description 针对表【tb_sites】的数据库操作Service实现
* @createDate 2025-09-17 18:44:53
*/
@Service
public class TbSitesServiceImpl extends ServiceImpl<TbSitesMapper, TbSites>
    implements TbSitesService{

    @Autowired
    private TbSysSitePlatformsMapper sysSitePlatformsMapper;

    @Override
    public List<TbSites> selectBySiteIds(List<String> siteIds) {
        return baseMapper.selectList(new LambdaQueryWrapper<TbSites>().in(TbSites::getSiteID, siteIds).eq(TbSites::getIsDelete, 0));
    }

    @Override
    public List<String> selectSiteIdsByPlatId(Integer platId) {
        return sysSitePlatformsMapper.selectSiteIdsByPlatId(platId);
    }


}




