package com.skysys.service.model.entity.slave1;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tb_sites
 */
@TableName(value ="tb_sites")
@Data
public class TbSites implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 站点编号
     */
    @TableField(value = "siteID")
    private String siteID;

    /**
     * 站点模式 1-固定机库+射频基站 2-射频基站 3-单兵中枢 4-移动机库 5-无人机遥控器 6-天枢
     */
    @TableField(value = "siteMode")
    private Integer siteMode;

    /**
     * 站点地址
     */
    @TableField(value = "siteAddress")
    private String siteAddress;

    /**
     * 站点名称
     */
    @TableField(value = "siteName")
    private String siteName;

    /**
     * 站点位置 WGS-84,经度
     */
    @TableField(value = "siteLocLongitude")
    private Double siteLocLongitude;

    /**
     * 站点位置 WGS-84,纬度
     */
    @TableField(value = "siteLocLatitude")
    private Double siteLocLatitude;

    /**
     * 站点备降位置 WGS-84,经度
     */
    @TableField(value = "siteOptionLocLongitude")
    private Double siteOptionLocLongitude;

    /**
     * 站点备降位置 WGS-84 纬度
     */
    @TableField(value = "siteOptionLocLatitude")
    private Double siteOptionLocLatitude;

    /**
     * 是否删除  1删除 0不删除
     */
    @TableField(value = "isDelete")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private String createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private String updateTime;

    /**
     * 站点相对地面高度
     */
    @TableField(value = "siteAltitude")
    private Double siteAltitude;

    /**
     * 站点海拔高度
     */
    @TableField(value = "siteHAltitude")
    private Double siteHAltitude;

    /**
     * 站点椭球高度
     */
    @TableField(value = "siteEllipsAltitude")
    private Double siteEllipsAltitude;

    /**
     * 站点无人机返航高度
     */
    @TableField(value = "siteRHAltitude")
    private Double siteRHAltitude;

    /**
     * 站点无人机返航降落悬停高度
     */
    @TableField(value = "siteRHHoverAltitude")
    private Double siteRHHoverAltitude;

    /**
     * 站点无人机备降点相对地面高度
     */
    @TableField(value = "siteOptionAltitude")
    private Double siteOptionAltitude;

    /**
     * 站点无人机备降悬停高度
     */
    @TableField(value = "siteOptionHoverAltitude")
    private Double siteOptionHoverAltitude;

    /**
     * 备降转移高度,无人机自动降落失败后前往备降点的相对高度,单位:米
     */
    @TableField(value = "siteOptionDivertHeight")
    private Double siteOptionDivertHeight;

    /**
     * 站点飞行模式  1-孤岛模式 2-跳棋模式
     */
    @TableField(value = "siteFlightMode")
    private Integer siteFlightMode;

    /**
     * 创建者
     */
    @TableField(value = "createUser")
    private String createUser;

    /**
     * 更新者
     */
    @TableField(value = "updateUser")
    private String updateUser;

    /**
     * 站点类型  1站点类型（带机库） 0单机
     */
    @TableField(value = "isSiteType")
    private Integer isSiteType;

    /**
     *  无人机控制协议:1-星逻无人机设备孤岛模式上云协议,2-星逻无人机设备网联通用上云协议,3-星逻无人机设备网联模式上云协议';
     */
    @TableField(value = "UAVCPV")
    private Integer UAVCPV;

    /**
     *  机库控制协议:4-星逻机库设备孤岛模式上云协议,5-星逻机库设备网联通用模式上云协议,6-星逻机库设备网联模式上云协议,7-大疆机库设备上云协议,8-小机场机库设备协议;
     */
    @TableField(value = "HIVECPV")
    private Integer HIVECPV;

    /**
     * 成果相关配置
     */
    @TableField(value = "resultConfig")
    private String resultConfig;

    /**
     * 站点背景图url
     */
    @TableField(value = "siteBkgUrl")
    private String siteBkgUrl;

    /**
     * 创建人ID
     */
    @TableField(value = "creatorID")
    private Long creatorID;

    /**
     * 更新人ID
     */
    @TableField(value = "updatorID")
    private Long updatorID;

    /**
     * 备降点海拔高度
     */
    @TableField(value = "siteOptionHAltitude")
    private Double siteOptionHAltitude;

    /**
     * 备降点椭球高度
     */
    @TableField(value = "siteOptionEllipsAltitude")
    private Double siteOptionEllipsAltitude;

    /**
     * 无人机起飞高度
     */
    @TableField(value = "UAVSTAltitude")
    private Double UAVSTAltitude;

    /**
     * 无人机备降点返航高度
     */
    @TableField(value = "siteOptionRHAltitude")
    private Double siteOptionRHAltitude;

    /**
     * 站点控制模式;1-生产模式;2-维护模式;
     */
    @TableField(value = "actionControlMode")
    private Integer actionControlMode;

    /**
     * 调度服务类型:1-V3;2-V4;
     */
    @TableField(value = "scheduleServiceType")
    private Integer scheduleServiceType;

    /**
     * 站点控制类型;1-实物控制;2-半实物控制;3-虚拟仿真控制
     */
    @TableField(value = "siteControlType")
    private Integer siteControlType;

    /**
     * 是否使用自定义备降信息:1-使用,2-不使用
     */
    @TableField(value = "siteOptionMode")
    private Integer siteOptionMode;

    /**
     * 是否使用MOP双联路模式,0-不使用;1-使用;
     */
    @TableField(value = "isUseMOPMode")
    private Integer isUseMOPMode;

    /**
     * 射频信号覆盖范围(米),孤岛模式站点使用，用于校验航点距离站点距离
     */
    @TableField(value = "signalRange")
    private Integer signalRange;

    /**
     * 
     */
    @TableField(value = "isMultipleUAV")
    private Integer isMultipleUAV;

    /**
     * 站点媒体容量（byte）
     */
    @TableField(value = "siteMediaCapacity")
    private Double siteMediaCapacity;

    /**
     * 飞行历史录屏容量（byte）
     */
    @TableField(value = "siteRecordCapacity")
    private Double siteRecordCapacity;

    /**
     * 站点视角配置
     */
    @TableField(value = "viewLiveConfig")
    private Object viewLiveConfig;

    /**
     * RTK配置信息
     */
    @TableField(value = "RTKConfig")
    private Object RTKConfig;

    /**
     * 是否使用临时凭证 0-不使用;1-使用
     */
    @TableField(value = "useOSSTempCert")
    private Integer useOSSTempCert;

    /**
     * [废弃，合并到resultConfig字段]1 自动上传 2定时上传 3手动上传 0不上传
     */
    @TableField(value = "mediaUploadType")
    private Integer mediaUploadType;

    /**
     * [废弃]推流域名
     */
    @TableField(value = "videoPushDomain")
    private String videoPushDomain;

    /**
     * [废弃]拉流域名
     */
    @TableField(value = "videoPullDomain")
    private String videoPullDomain;

    /**
     * [废弃]是否检查覆盖范围,1-使用,2-不使用,默认:2
     */
    @TableField(value = "isUseCheckSignalRange")
    private Integer isUseCheckSignalRange;

    /**
     * [废弃]支持的自定义图层类型,1-二维扫图,2-三维建筑模型,3-点云
     */
    @TableField(value = "supportCustomLayerTypes")
    private String supportCustomLayerTypes;

    /**
     * [废弃]站点安全位置 WGS-84 纬度
     */
    @TableField(value = "siteSafeLocLatitude")
    private Double siteSafeLocLatitude;

    /**
     * [废弃]站点安全位置 WGS-84 经度
     */
    @TableField(value = "siteSafeLocLongitude")
    private Double siteSafeLocLongitude;

    /**
     * [废弃]站点无人机降落朝向
     */
    @TableField(value = "siteRHHeading")
    private Double siteRHHeading;

    /**
     * [废弃]站点备降朝向
     */
    @TableField(value = "siteOptionHeading")
    private Double siteOptionHeading;

    /**
     * [废弃]站点第三视角flv地址
     */
    @TableField(value = "siteFLVURI")
    private String siteFLVURI;

    /**
     * [废弃]项目编号
     */
    @TableField(value = "PID")
    private String PID;

    /**
     * [废弃]企业编号
     */
    @TableField(value = "CPID")
    private String CPID;

    /**
     * [废弃]网络模式  1-互联网  2-局域网
     */
    @TableField(value = "networkMode")
    private Integer networkMode;

    /**
     * [废弃]类型5除外,其他类型是否配置机载计算机 默认是否 0
     */
    @TableField(value = "isComputer")
    private Integer isComputer;

    /**
     * [废弃]支持的飞行方式 1普通飞行 2精细化飞
     */
    @TableField(value = "flightWay")
    private Integer flightWay;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}