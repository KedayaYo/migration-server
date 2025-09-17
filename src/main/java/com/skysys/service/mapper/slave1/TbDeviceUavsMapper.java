package com.skysys.service.mapper.slave1;

import com.skysys.service.model.entity.slave1.TbDeviceUavs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author entic
* @description 针对表【tb_device_uavs】的数据库操作Mapper
* @createDate 2025-09-17 18:44:53
* @Entity com.skysys.service.model.entity.slave1.TbDeviceUavs
*/
public interface TbDeviceUavsMapper extends BaseMapper<TbDeviceUavs> {

    @Select("SELECT DISTINCT u.UAVID FROM tb_device_uavs u " +
            "INNER JOIN tb_sys_uav_platforms up ON u.UAVID = up.UAVID " +
            "WHERE up.PLATID = #{platId} AND u.isDelete = 0 " +
            "AND u.SN NOT LIKE '%111%' AND u.SN NOT LIKE '%222%' " +
            "AND u.SN NOT LIKE '%000%' AND u.SN NOT LIKE '%333%' " +
            "AND u.SN NOT LIKE '%444%' AND u.SN NOT LIKE '%555%' " +
            "AND u.SN NOT LIKE '%123123%' AND u.SN NOT LIKE '%aswe%' " +
            "AND u.SN NOT LIKE '%fsad%'")
    List<String> selectUavIdsByPlatId(@Param("platId") Integer platId);
}




