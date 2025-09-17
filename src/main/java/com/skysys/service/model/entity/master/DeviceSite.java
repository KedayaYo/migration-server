package com.skysys.service.model.entity.master;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.skysys.service.handler.Fastjson2ArrayTypeHandler;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @TableName device_site
 */
@TableName(value = "device_site")
@Data
public class DeviceSite implements Serializable {
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
     * 创建者
     */
    private String createUser;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新者
     */
    private String updateUser;

    /**
     * 更新时间
     */
    private String updateTime;

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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        DeviceSite other = (DeviceSite) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getSiteId() == null ? other.getSiteId() == null : this.getSiteId().equals(other.getSiteId()))
                && (this.getSiteName() == null ? other.getSiteName() == null : this.getSiteName().equals(other.getSiteName()))
                && (this.getSiteAddress() == null ? other.getSiteAddress() == null : this.getSiteAddress().equals(other.getSiteAddress()))
                && (this.getSiteMode() == null ? other.getSiteMode() == null : this.getSiteMode().equals(other.getSiteMode()))
                && (this.getSiteLocation() == null ? other.getSiteLocation() == null : this.getSiteLocation().equals(other.getSiteLocation()))
                && (this.getSiteOptionLocation() == null ? other.getSiteOptionLocation() == null : this.getSiteOptionLocation().equals(other.getSiteOptionLocation()))
                && (this.getSiteSafeLocation() == null ? other.getSiteSafeLocation() == null : this.getSiteSafeLocation().equals(other.getSiteSafeLocation()))
                && (this.getSiteAltitude() == null ? other.getSiteAltitude() == null : this.getSiteAltitude().equals(other.getSiteAltitude()))
                && (this.getSiteHAltitude() == null ? other.getSiteHAltitude() == null : this.getSiteHAltitude().equals(other.getSiteHAltitude()))
                && (this.getSiteEllipsAltitude() == null ? other.getSiteEllipsAltitude() == null : this.getSiteEllipsAltitude().equals(other.getSiteEllipsAltitude()))
                && (this.getSiteRhAltitude() == null ? other.getSiteRhAltitude() == null : this.getSiteRhAltitude().equals(other.getSiteRhAltitude()))
                && (this.getUavStAltitude() == null ? other.getUavStAltitude() == null : this.getUavStAltitude().equals(other.getUavStAltitude()))
                && (this.getVideoPullAddr() == null ? other.getVideoPullAddr() == null : this.getVideoPullAddr().equals(other.getVideoPullAddr()))
                && (this.getVideoPushAddr() == null ? other.getVideoPushAddr() == null : this.getVideoPushAddr().equals(other.getVideoPushAddr()))
                && (this.getSiteFlvUrl() == null ? other.getSiteFlvUrl() == null : this.getSiteFlvUrl().equals(other.getSiteFlvUrl()))
                && (this.getSiteRhHeading() == null ? other.getSiteRhHeading() == null : this.getSiteRhHeading().equals(other.getSiteRhHeading()))
                && (this.getSiteRhHoverAltitude() == null ? other.getSiteRhHoverAltitude() == null : this.getSiteRhHoverAltitude().equals(other.getSiteRhHoverAltitude()))
                && (this.getSiteOptionAltitude() == null ? other.getSiteOptionAltitude() == null : this.getSiteOptionAltitude().equals(other.getSiteOptionAltitude()))
                && (this.getSiteOptionHAltitude() == null ? other.getSiteOptionHAltitude() == null : this.getSiteOptionHAltitude().equals(other.getSiteOptionHAltitude()))
                && (this.getSiteOptionEllipsAltitude() == null ? other.getSiteOptionEllipsAltitude() == null : this.getSiteOptionEllipsAltitude().equals(other.getSiteOptionEllipsAltitude()))
                && (this.getSiteOptionHeading() == null ? other.getSiteOptionHeading() == null : this.getSiteOptionHeading().equals(other.getSiteOptionHeading()))
                && (this.getSiteOptionHoverAltitude() == null ? other.getSiteOptionHoverAltitude() == null : this.getSiteOptionHoverAltitude().equals(other.getSiteOptionHoverAltitude()))
                && (this.getSiteOptionRhAltitude() == null ? other.getSiteOptionRhAltitude() == null : this.getSiteOptionRhAltitude().equals(other.getSiteOptionRhAltitude()))
                && (this.getSiteSfMode() == null ? other.getSiteSfMode() == null : this.getSiteSfMode().equals(other.getSiteSfMode()))
                && (this.getUavId() == null ? other.getUavId() == null : this.getUavId().equals(other.getUavId()))
                && (this.getEcid1() == null ? other.getEcid1() == null : this.getEcid1().equals(other.getEcid1()))
                && (this.getSiteBkgUrl() == null ? other.getSiteBkgUrl() == null : this.getSiteBkgUrl().equals(other.getSiteBkgUrl()))
                && (this.getEcid2() == null ? other.getEcid2() == null : this.getEcid2().equals(other.getEcid2()))
                && (this.getFlightWay() == null ? other.getFlightWay() == null : this.getFlightWay().equals(other.getFlightWay()))
                && (this.getActionControlMode() == null ? other.getActionControlMode() == null : this.getActionControlMode().equals(other.getActionControlMode()))
                && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
                && (this.getIsSiteType() == null ? other.getIsSiteType() == null : this.getIsSiteType().equals(other.getIsSiteType()))
                && (this.getHiveId() == null ? other.getHiveId() == null : this.getHiveId().equals(other.getHiveId()))
                && (this.getSiteOptionMode() == null ? other.getSiteOptionMode() == null : this.getSiteOptionMode().equals(other.getSiteOptionMode()))
                && (this.getIsUseMopMode() == null ? other.getIsUseMopMode() == null : this.getIsUseMopMode().equals(other.getIsUseMopMode()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSiteId() == null) ? 0 : getSiteId().hashCode());
        result = prime * result + ((getSiteName() == null) ? 0 : getSiteName().hashCode());
        result = prime * result + ((getSiteAddress() == null) ? 0 : getSiteAddress().hashCode());
        result = prime * result + ((getSiteMode() == null) ? 0 : getSiteMode().hashCode());
        result = prime * result + ((getSiteLocation() == null) ? 0 : getSiteLocation().hashCode());
        result = prime * result + ((getSiteOptionLocation() == null) ? 0 : getSiteOptionLocation().hashCode());
        result = prime * result + ((getSiteSafeLocation() == null) ? 0 : getSiteSafeLocation().hashCode());
        result = prime * result + ((getSiteAltitude() == null) ? 0 : getSiteAltitude().hashCode());
        result = prime * result + ((getSiteHAltitude() == null) ? 0 : getSiteHAltitude().hashCode());
        result = prime * result + ((getSiteEllipsAltitude() == null) ? 0 : getSiteEllipsAltitude().hashCode());
        result = prime * result + ((getSiteRhAltitude() == null) ? 0 : getSiteRhAltitude().hashCode());
        result = prime * result + ((getUavStAltitude() == null) ? 0 : getUavStAltitude().hashCode());
        result = prime * result + ((getVideoPullAddr() == null) ? 0 : getVideoPullAddr().hashCode());
        result = prime * result + ((getVideoPushAddr() == null) ? 0 : getVideoPushAddr().hashCode());
        result = prime * result + ((getSiteFlvUrl() == null) ? 0 : getSiteFlvUrl().hashCode());
        result = prime * result + ((getSiteRhHeading() == null) ? 0 : getSiteRhHeading().hashCode());
        result = prime * result + ((getSiteRhHoverAltitude() == null) ? 0 : getSiteRhHoverAltitude().hashCode());
        result = prime * result + ((getSiteOptionAltitude() == null) ? 0 : getSiteOptionAltitude().hashCode());
        result = prime * result + ((getSiteOptionHAltitude() == null) ? 0 : getSiteOptionHAltitude().hashCode());
        result = prime * result + ((getSiteOptionEllipsAltitude() == null) ? 0 : getSiteOptionEllipsAltitude().hashCode());
        result = prime * result + ((getSiteOptionHeading() == null) ? 0 : getSiteOptionHeading().hashCode());
        result = prime * result + ((getSiteOptionHoverAltitude() == null) ? 0 : getSiteOptionHoverAltitude().hashCode());
        result = prime * result + ((getSiteOptionRhAltitude() == null) ? 0 : getSiteOptionRhAltitude().hashCode());
        result = prime * result + ((getSiteSfMode() == null) ? 0 : getSiteSfMode().hashCode());
        result = prime * result + ((getUavId() == null) ? 0 : getUavId().hashCode());
        result = prime * result + ((getEcid1() == null) ? 0 : getEcid1().hashCode());
        result = prime * result + ((getSiteBkgUrl() == null) ? 0 : getSiteBkgUrl().hashCode());
        result = prime * result + ((getEcid2() == null) ? 0 : getEcid2().hashCode());
        result = prime * result + ((getFlightWay() == null) ? 0 : getFlightWay().hashCode());
        result = prime * result + ((getActionControlMode() == null) ? 0 : getActionControlMode().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        result = prime * result + ((getIsSiteType() == null) ? 0 : getIsSiteType().hashCode());
        result = prime * result + ((getHiveId() == null) ? 0 : getHiveId().hashCode());
        result = prime * result + ((getSiteOptionMode() == null) ? 0 : getSiteOptionMode().hashCode());
        result = prime * result + ((getIsUseMopMode() == null) ? 0 : getIsUseMopMode().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", siteId=").append(siteId);
        sb.append(", siteName=").append(siteName);
        sb.append(", siteAddress=").append(siteAddress);
        sb.append(", siteMode=").append(siteMode);
        sb.append(", siteLocation=").append(siteLocation);
        sb.append(", siteOptionLocation=").append(siteOptionLocation);
        sb.append(", siteSafeLocation=").append(siteSafeLocation);
        sb.append(", siteAltitude=").append(siteAltitude);
        sb.append(", siteHAltitude=").append(siteHAltitude);
        sb.append(", siteEllipsAltitude=").append(siteEllipsAltitude);
        sb.append(", siteRhAltitude=").append(siteRhAltitude);
        sb.append(", uavStAltitude=").append(uavStAltitude);
        sb.append(", videoPullAddr=").append(videoPullAddr);
        sb.append(", videoPushAddr=").append(videoPushAddr);
        sb.append(", siteFlvUrl=").append(siteFlvUrl);
        sb.append(", siteRhHeading=").append(siteRhHeading);
        sb.append(", siteRhHoverAltitude=").append(siteRhHoverAltitude);
        sb.append(", siteOptionAltitude=").append(siteOptionAltitude);
        sb.append(", siteOptionHAltitude=").append(siteOptionHAltitude);
        sb.append(", siteOptionEllipsAltitude=").append(siteOptionEllipsAltitude);
        sb.append(", siteOptionHeading=").append(siteOptionHeading);
        sb.append(", siteOptionHoverAltitude=").append(siteOptionHoverAltitude);
        sb.append(", siteOptionRhAltitude=").append(siteOptionRhAltitude);
        sb.append(", siteSfMode=").append(siteSfMode);
        sb.append(", uavId=").append(uavId);
        sb.append(", ecid1=").append(ecid1);
        sb.append(", siteBkgUrl=").append(siteBkgUrl);
        sb.append(", ecid2=").append(ecid2);
        sb.append(", flightWay=").append(flightWay);
        sb.append(", actionControlMode=").append(actionControlMode);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", isSiteType=").append(isSiteType);
        sb.append(", hiveId=").append(hiveId);
        sb.append(", siteOptionMode=").append(siteOptionMode);
        sb.append(", isUseMopMode=").append(isUseMopMode);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}