package com.skysys.service.mapper.slave1;

import com.skysys.service.model.entity.slave1.TbDeviceHives;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author entic
* @description 针对表【tb_device_hives】的数据库操作Mapper
* @createDate 2025-09-17 18:44:53
* @Entity com.skysys.service.model.entity.slave1.TbDeviceHives
*/
public interface TbDeviceHivesMapper extends BaseMapper<TbDeviceHives> {

    @Select("SELECT DISTINCT h.hiveID FROM tb_device_hives h " +
            "INNER JOIN tb_sys_hive_platforms hp ON h.hiveID = hp.hiveID " +
            "WHERE hp.PLATID = #{platId} AND h.isDelete = 0 " +
            "AND h.hiveID NOT LIKE '%11%' AND h.hiveID NOT LIKE '%222%' " +
            "AND h.hiveID NOT LIKE '%0000%' AND h.hiveID NOT LIKE '%333%' " +
            "AND h.hiveID NOT LIKE '%444%' AND h.hiveID NOT LIKE '%555%' " +
            "AND h.hiveID NOT LIKE '%123123%' AND h.hiveID NOT LIKE '%aswe%' " +
            "AND h.hiveID NOT LIKE '%fsad%' AND h.hiveID NOT LIKE '%4651%' " +
            "AND h.hiveID NOT LIKE '%werw%' AND h.hiveName NOT LIKE '%111%' " +
            "AND h.hiveName NOT LIKE '%222%'")
    List<String> selectHiveIdsByPlatId(@Param("platId") Integer platId);
}




