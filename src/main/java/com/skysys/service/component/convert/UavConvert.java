package com.skysys.service.component.convert;

import com.skysys.service.model.entity.master.DeviceUav;
import com.skysys.service.model.entity.slave1.TbDeviceUavs;
import com.skysys.service.model.entity.slave1.TbSysDeviceModels;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * UAV数据映射器
 */
@Mapper(componentModel = "spring")
public interface UavConvert {

    /**
     * TbDeviceUavs 转 DeviceUav
     */
    @Mappings({
            // 基础字段映射
            @Mapping(source = "tbDeviceUavs.UAVID", target = "uavId"),
            @Mapping(source = "tbDeviceUavs.UAVName", target = "uavName"),
            @Mapping(source = "tbDeviceUavs.siteID", target = "siteId"),
            @Mapping(source = "tbDeviceUavs.status", target = "status"),
            @Mapping(source = "tbDeviceUavs.FCSN", target = "fcsn"),
            @Mapping(source = "tbDeviceUavs.SN", target = "sn"),
            @Mapping(source = "tbDeviceUavs.ECID", target = "ecid1"),
            @Mapping(source = "tbDeviceUavs.UAVFLVURL", target = "uavFlvUrl"),
            @Mapping(source = "tbDeviceUavs.isHaveComputer", target = "isHaveComputer", qualifiedByName = "integerToString"),
            
            // 从TbSysDeviceModels获取的字段
            @Mapping(source = "deviceModel.brand", target = "brand"),
            @Mapping(source = "deviceModel.modelCode", target = "model"),
            
            // 用户信息映射
            @Mapping(source = "tbDeviceUavs.createUser", target = "createUser"),
            @Mapping(source = "tbDeviceUavs.updateUser", target = "updateUser"),
            
            // 时间字段映射
            @Mapping(source = "tbDeviceUavs.createTime", target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "tbDeviceUavs.updateTime", target = "updateTime"),
            
            // 删除标记映射
            @Mapping(source = "tbDeviceUavs.isDelete", target = "deleted"),
            
            // 忽略的字段
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "ecid2", ignore = true),
            @Mapping(target = "uavLocation", ignore = true)
    })
    DeviceUav convert(TbDeviceUavs tbDeviceUavs, TbSysDeviceModels deviceModel);

    /**
     * Integer转String的辅助方法
     */
    @Named("integerToString")
    default String integerToString(Integer value) {
        return value == null ? null : value.toString();
    }
}
