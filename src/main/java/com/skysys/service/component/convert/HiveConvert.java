package com.skysys.service.component.convert;

import com.skysys.service.model.entity.master.DeviceHive;
import com.skysys.service.model.entity.slave1.TbDeviceHives;
import com.skysys.service.model.entity.slave1.TbSysDeviceModels;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

/**
 * Hive数据映射器
 */
@Mapper(componentModel = "spring")
public interface HiveConvert {

    /**
     * TbDeviceHives 转 DeviceHive
     */
    @Mappings({
            // 基础字段映射
            @Mapping(source = "tbDeviceHives.hiveID", target = "hiveId"),
            @Mapping(source = "tbDeviceHives.hiveName", target = "hiveName"),
            @Mapping(source = "tbDeviceHives.hiveType", target = "hiveType"),
            @Mapping(source = "tbDeviceHives.siteID", target = "siteId"),
            @Mapping(source = "tbDeviceHives.hiveFLVURL", target = "hiveFlvUrl"),
            
            // 从TbSysDeviceModels获取hiveModel字段
            @Mapping(source = "deviceModel.modelCode", target = "hiveModel"),

            // 用户信息映射
            // @Mapping(source = "createUser", target = "createUser"),
            // @Mapping(source = "updateUser", target = "updateUser"),
            @Mapping(target = "createUser", ignore = true),
            @Mapping(target = "updateUser", ignore = true),

            // 时间字段映射 - 直接映射字符串
            // @Mapping(source = "createTime", target = "createTime"),
            // @Mapping(source = "updateTime", target = "updateTime"),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            
            // 删除标记映射 (isDelete -> deleted)
            @Mapping(source = "tbDeviceHives.isDelete", target = "deleted", qualifiedByName = "convertDeleteFlag"),

            // 忽略的字段
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "removeFault", ignore = true),
            @Mapping(target = "ugvId", ignore = true)
    })
    DeviceHive convert(TbDeviceHives tbDeviceHives, TbSysDeviceModels deviceModel);

    /**
     * 转换删除标记：isDelete (0未删除,1已删除) -> deleted (字符串类型)
     */
    @Named("convertDeleteFlag")
    default String convertDeleteFlag(Integer isDelete) {
        if (isDelete == null) {
            return "0";
        }
        return isDelete == 0 ? "0" : "1";
    }
}
