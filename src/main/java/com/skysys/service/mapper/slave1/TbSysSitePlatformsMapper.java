package com.skysys.service.mapper.slave1;

import com.skysys.service.model.entity.slave1.TbSysSitePlatforms;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author entic
* @description 针对表【tb_sys_site_platforms】的数据库操作Mapper
* @createDate 2025-09-17 21:22:20
* @Entity com.skysys.service.model.entity.slave1.TbSysSitePlatforms
*/
public interface TbSysSitePlatformsMapper extends BaseMapper<TbSysSitePlatforms> {

    @Select("SELECT DISTINCT s.siteID FROM tb_sites s " +
            "INNER JOIN tb_sys_site_platforms sp ON s.id = sp.siteID " +
            "WHERE sp.PLATID = #{platId} AND s.isDelete = 0 " +
            "AND s.siteName NOT LIKE '%1%' AND s.siteName NOT LIKE '%2%' " +
            "AND s.siteName NOT LIKE '%qq%' AND s.siteName NOT LIKE '%test%' " +
            "AND s.siteName NOT LIKE '%ces%'")
    List<String> selectSiteIdsByPlatId(@Param("platId") Integer platId);
}




