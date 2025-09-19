package com.skysys.service.model.entity.master;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.skysys.service.handler.Fastjson2ArrayTypeHandler;
import com.skysys.service.model.entity.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @TableName device_site
 */
// @TableName(value = "device_site")
@TableName(value = "device_site_test")
@Data
public class DeviceSite extends BaseModel implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统编号
     */
    private String siteId;

    /**
     * 系统名称
     */
    private String siteName;

    /**
     * 系统详细地址
     */
    private String siteAddress;

    /**
     * 配置模式 1-机库+射频箱 2-射频箱 3-单兵中枢 4-遥控器 5-机载计算机
     */
    private Integer siteMode;

    /**
     * 系统位置 -系统类型为站点时，必填
     */
    @TableField(typeHandler = Fastjson2ArrayTypeHandler.class)
    private Double[] siteLocation;

    /**
     * 系统类型为站点类型，机库模式，系统备降位置
     */
    @TableField(typeHandler = Fastjson2ArrayTypeHandler.class)
    private Double[] siteOptionLocation;

    /**
     * 系统类型为站点类型，机库模式，系统安全位置
     */
    @TableField(typeHandler = Fastjson2ArrayTypeHandler.class)
    private Double[] siteSafeLocation;

    /**
     * 系统相对地面高度 -系统类型为站点时，必填
     */
    private Double siteAltitude;

    /**
     * 系统海拔高度
     */
    private Double siteHAltitude;

    /**
     * 系统椭球高度
     */
    private Double siteEllipsAltitude;

    /**
     * 系统无人机返航高度
     */
    private Double siteRhAltitude;

    /**
     * 无人机起飞飞行高度
     */
    private Double uavStAltitude;

    /**
     * 系统直播拉流配置地址
     */
    private String videoPullAddr;

    /**
     * 系统直播推流配置地址
     */
    private String videoPushAddr;

    /**
     * 系统FLV实时视角地址
     */
    private String siteFlvUrl;

    /**
     * 系统类型为站点类型，机库模式，无人机返航朝向 [-180,180] 默认0
     */
    private Double siteRhHeading;

    /**
     * 系统类型为站点类型，机库模式，无人机返航悬停海拔高度
     */
    private String siteRhHoverAltitude;

    /**
     * 系统类型为站点类型，机库模式，无人机备降点相对地面高度，机库的模式使用
     */
    private String siteOptionAltitude;

    /**
     * 系统类型为站点类型，机库模式，无人机备降点海拔高度，机库的模式使用
     */
    private String siteOptionHAltitude;

    /**
     * 系统类型为站点类型，机库模式，无人机备降点椭球高度，机库的模式使用
     */
    private String siteOptionEllipsAltitude;

    /**
     * 系统类型为站点类型，机库模式，无人机备降点朝向[-180,180]，机库模式使用
     */
    private String siteOptionHeading;

    /**
     * 系统类型为站点类型，机库模式，无人机备降点悬停海拔高度，机库模式使用
     */
    private String siteOptionHoverAltitude;

    /**
     * 系统类型为站点类型，机库模式，无人机备降点返航高度
     */
    private String siteOptionRhAltitude;

    /**
     * 系统支持的飞行模式 1-孤岛模式 2-跳棋模式
     */
    private Integer siteSfMode;

    /**
     * 系统类型为单机类型时，无人机编号必填
     */
    private String uavId;

    /**
     * 系统类型为单机类型时，需要根据配置模式 填写边缘计算机ID
     */
    private String ecid1;

    /**
     * 站点背景图片Url
     */
    private String siteBkgUrl;

    /**
     * 系统类型为单机类型时, 需要根据配置模式填入边缘计算机编号
     */
    private String ecid2;

    /**
     * 飞行方式 1:普通巡航 2:精细化飞行
     */
    private Integer flightWay;

    /**
     * 动作控制模式： 1 生产模式   2维护模式
     */
    private Integer actionControlMode;

    /**
     * 删除标记
     */
    private Integer deleted;

    /**
     * 是否是点位类型 1-站点 0-单机 -1-查询全部
     * 是否是点位类型 1-站点 0-单机 -1-查询全部
     * 是否是点位类型  1-站点 0-单机  -1-查询全部
     */
    private Integer isSiteType;

    /**
     * 机库Id
     */
    private String hiveId;

    /**
     * 是否使用自定义备降信息,1-使用,2-不使用
     */
    private Integer siteOptionMode;

    /**
     * 是否使用MOP双链路模式 0:不使用，1:使用
     */
    private Integer isUseMopMode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}