package com.skysys.service.model.entity.master;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName device_uav
 */
@TableName(value ="device_uav")
@Data
public class DeviceUav implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 无人机编号
     */
    private String uavId;

    /**
     * 无人机名称
     */
    private String uavName;

    /**
     * 无人机生产制造商
     */
    private String brand;

    /**
     * 无人机型号
     */
    private String model;

    /**
     * 无人机所属的站点
     */
    private String siteId;

    /**
     * 状态  1入库  2在役  3维修 4废弃
     */
    private Integer status;

    /**
     * 无人机飞控SN
     */
    private String fcsn;

    /**
     * SN
     */
    private String sn;

    /**
     * 边缘计算机1
     */
    private String ecid1;

    /**
     * 边缘计算机2
     */
    private String ecid2;

    /**
     * 无人机FLV实时视角地址
     */
    private String uavFlvUrl;

    /**
     * 是否配置边缘计算机 1:已配置 2:未配置
     */
    private String isHaveComputer;

    /**
     * 创建者
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

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
     * 无人机当前的实时位置-[经度，纬度] -WGS84
     */
    private String uavLocation;

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
        DeviceUav other = (DeviceUav) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUavId() == null ? other.getUavId() == null : this.getUavId().equals(other.getUavId()))
            && (this.getUavName() == null ? other.getUavName() == null : this.getUavName().equals(other.getUavName()))
            && (this.getBrand() == null ? other.getBrand() == null : this.getBrand().equals(other.getBrand()))
            && (this.getModel() == null ? other.getModel() == null : this.getModel().equals(other.getModel()))
            && (this.getSiteId() == null ? other.getSiteId() == null : this.getSiteId().equals(other.getSiteId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getFcsn() == null ? other.getFcsn() == null : this.getFcsn().equals(other.getFcsn()))
            && (this.getSn() == null ? other.getSn() == null : this.getSn().equals(other.getSn()))
            && (this.getEcid1() == null ? other.getEcid1() == null : this.getEcid1().equals(other.getEcid1()))
            && (this.getEcid2() == null ? other.getEcid2() == null : this.getEcid2().equals(other.getEcid2()))
            && (this.getUavFlvUrl() == null ? other.getUavFlvUrl() == null : this.getUavFlvUrl().equals(other.getUavFlvUrl()))
            && (this.getIsHaveComputer() == null ? other.getIsHaveComputer() == null : this.getIsHaveComputer().equals(other.getIsHaveComputer()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
            && (this.getUavLocation() == null ? other.getUavLocation() == null : this.getUavLocation().equals(other.getUavLocation()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUavId() == null) ? 0 : getUavId().hashCode());
        result = prime * result + ((getUavName() == null) ? 0 : getUavName().hashCode());
        result = prime * result + ((getBrand() == null) ? 0 : getBrand().hashCode());
        result = prime * result + ((getModel() == null) ? 0 : getModel().hashCode());
        result = prime * result + ((getSiteId() == null) ? 0 : getSiteId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getFcsn() == null) ? 0 : getFcsn().hashCode());
        result = prime * result + ((getSn() == null) ? 0 : getSn().hashCode());
        result = prime * result + ((getEcid1() == null) ? 0 : getEcid1().hashCode());
        result = prime * result + ((getEcid2() == null) ? 0 : getEcid2().hashCode());
        result = prime * result + ((getUavFlvUrl() == null) ? 0 : getUavFlvUrl().hashCode());
        result = prime * result + ((getIsHaveComputer() == null) ? 0 : getIsHaveComputer().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        result = prime * result + ((getUavLocation() == null) ? 0 : getUavLocation().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uavId=").append(uavId);
        sb.append(", uavName=").append(uavName);
        sb.append(", brand=").append(brand);
        sb.append(", model=").append(model);
        sb.append(", siteId=").append(siteId);
        sb.append(", status=").append(status);
        sb.append(", fcsn=").append(fcsn);
        sb.append(", sn=").append(sn);
        sb.append(", ecid1=").append(ecid1);
        sb.append(", ecid2=").append(ecid2);
        sb.append(", uavFlvUrl=").append(uavFlvUrl);
        sb.append(", isHaveComputer=").append(isHaveComputer);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", uavLocation=").append(uavLocation);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}