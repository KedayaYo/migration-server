package com.skysys.service.service.slave1;

import com.skysys.service.model.entity.slave1.TbSites;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author entic
* @description 针对表【tb_sites】的数据库操作Service
* @createDate 2025-09-17 18:44:53
*/
public interface TbSitesService extends IService<TbSites> {


    /**
     * 根据站点ID列表查询站点信息
     * @param siteIds
     * @return
     */
    List<TbSites> selectBySiteIds(List<String> siteIds);

    /**
     * 根据平台ID查询站点ID列表
     */
    List<String> selectSiteIdsByPlatId(@Param("platId") Integer platId);

}
