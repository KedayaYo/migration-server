package com.skysys.service.component.convert;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.skysys.service.model.entity.master.DeviceSite;
import com.skysys.service.model.entity.slave1.TbSites;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 站点数据映射器
 *
 * @author: 陶添浩
 * @date: 2025/9/17 16:30:00
 */
@Mapper(componentModel = "spring")
public interface SiteConvert {

    /**
     * TbSites 转 DeviceSite
     */
    @Mappings({
            // 基础字段映射
            @Mapping(source = "siteID", target = "siteId"),
            @Mapping(source = "siteName", target = "siteName"),
            @Mapping(source = "siteAddress", target = "siteAddress"),
            @Mapping(source = "siteMode", target = "siteMode"),

            // 位置信息映射 - 转换为Double数组
            @Mapping(expression = "java(buildLocationArray(tbSites.getSiteLocLongitude(), tbSites.getSiteLocLatitude()))", target = "siteLocation"),
            @Mapping(expression = "java(buildLocationArray(tbSites.getSiteOptionLocLongitude(), tbSites.getSiteOptionLocLatitude()))", target = "siteOptionLocation"),

            // 高度相关字段映射
            @Mapping(source = "siteAltitude", target = "siteAltitude"),
            @Mapping(source = "siteHAltitude", target = "siteHAltitude"),
            @Mapping(source = "siteEllipsAltitude", target = "siteEllipsAltitude"),
            @Mapping(source = "siteRHAltitude", target = "siteRhAltitude"),
            @Mapping(source = "UAVSTAltitude", target = "uavStAltitude"),

            // 悬停高度等字段映射 - 转换为String类型
            @Mapping(expression = "java(doubleToString(tbSites.getSiteRHHoverAltitude()))", target = "siteRhHoverAltitude"),
            @Mapping(expression = "java(doubleToString(tbSites.getSiteOptionAltitude()))", target = "siteOptionAltitude"),
            @Mapping(expression = "java(doubleToString(tbSites.getSiteOptionHAltitude()))", target = "siteOptionHAltitude"),
            @Mapping(expression = "java(doubleToString(tbSites.getSiteOptionEllipsAltitude()))", target = "siteOptionEllipsAltitude"),
            @Mapping(expression = "java(doubleToString(tbSites.getSiteOptionHoverAltitude()))", target = "siteOptionHoverAltitude"),
            @Mapping(expression = "java(doubleToString(tbSites.getSiteOptionRHAltitude()))", target = "siteOptionRhAltitude"),

            // 飞行模式映射
            @Mapping(source = "siteFlightMode", target = "siteSfMode"),

            // 背景图URL映射
            @Mapping(source = "siteBkgUrl", target = "siteBkgUrl"),

            // 控制模式映射
            @Mapping(source = "actionControlMode", target = "actionControlMode"),

            // 用户信息映射
            @Mapping(source = "createUser", target = "createUser"),
            @Mapping(source = "updateUser", target = "updateUser"),

            // 站点类型映射
            @Mapping(source = "isSiteType", target = "isSiteType"),

            // 备降模式映射
            @Mapping(source = "siteOptionMode", target = "siteOptionMode"),

            // MOP模式映射
            @Mapping(source = "isUseMOPMode", target = "isUseMopMode"),

            // 删除标记映射
            @Mapping(source = "isDelete", target = "deleted"),

            // 视频配置映射 - 使用Object类型的viewLiveConfig
            @Mapping(source = "viewLiveConfig", target = "videoPullAddr", qualifiedByName = "buildVideoPullAddr"),
            @Mapping(source = "viewLiveConfig", target = "videoPushAddr", qualifiedByName = "buildVideoPushAddr"),

            // 时间字段映射 - 直接映射字符串
            @Mapping(source = "createTime", target = "createTime"),
            @Mapping(source = "updateTime", target = "updateTime"),

            // 忽略需要单独设置的字段
            @Mapping(target = "uavId", ignore = true),
            @Mapping(target = "hiveId", ignore = true),

            // 忽略目标类中没有对应源的字段
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "siteSafeLocation", ignore = true),
            @Mapping(target = "siteFlvUrl", ignore = true),
            @Mapping(target = "siteRhHeading", ignore = true),
            @Mapping(target = "siteOptionHeading", ignore = true),
            @Mapping(target = "ecid1", ignore = true),
            @Mapping(target = "ecid2", ignore = true),
            @Mapping(target = "flightWay", ignore = true)
    })
    DeviceSite convert(TbSites tbSites);

    /**
     * TbSites 列表转 DeviceSite 列表
     */
    List<DeviceSite> convert(List<TbSites> tbSitesList);

    /**
     * 构建位置数组（经纬度）
     * 返回格式: [经度, 纬度]
     */
    default Double[] buildLocationArray(Double longitude, Double latitude) {
        if (longitude == null || latitude == null || longitude == 0.0 || latitude == 0.0) {
            return null;
        }
        return new Double[]{longitude, latitude};
    }

    /**
     * Double转String的辅助方法
     */
    default String doubleToString(Double value) {
        return value == null ? null : value.toString();
    }

    /**
     * 从viewLiveConfig中提取videoPullAddr
     */
    @Named("buildVideoPullAddr")
    default String buildVideoPullAddr(Object viewLiveConfig) {
        if (viewLiveConfig == null) {
            return null;
        }

        try {
            String configJson;
            if (viewLiveConfig instanceof String) {
                configJson = (String) viewLiveConfig;
            } else {
                configJson = JSON.toJSONString(viewLiveConfig);
            }

            if (!StringUtils.hasText(configJson)) {
                return null;
            }

            Map<String, String> configMap = JSON.parseObject(configJson, new TypeReference<Map<String, String>>() {});
            return configMap.get("videoPullAddr");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从viewLiveConfig中提取videoPushAddr
     */
    @Named("buildVideoPushAddr")
    default String buildVideoPushAddr(Object viewLiveConfig) {
        if (viewLiveConfig == null) {
            return null;
        }

        try {
            String configJson;
            if (viewLiveConfig instanceof String) {
                configJson = (String) viewLiveConfig;
            } else {
                configJson = JSON.toJSONString(viewLiveConfig);
            }

            if (!StringUtils.hasText(configJson)) {
                return null;
            }

            Map<String, String> configMap = JSON.parseObject(configJson, new TypeReference<Map<String, String>>() {});
            return configMap.get("videoPushAddr");
        } catch (Exception e) {
            return null;
        }
    }
}
