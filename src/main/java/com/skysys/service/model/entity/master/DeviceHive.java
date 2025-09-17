package com.skysys.service.model.entity.master;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName device_hive
 */
@TableName(value ="device_hive")
@Data
public class DeviceHive implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 机库编号
     */
    private String hiveId;

    /**
     * 机库名称
     */
    private String hiveName;

    /**
     * 机库类型
     */
    private Integer hiveType;

    /**
     * 机库所属站点编号
     */
    private String siteId;

    /**
     * 机库内窥url是视角地址
     */
    private String hiveFlvUrl;

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
    private String deleted;

    /**
     * 解除故障 为1解除
     */
    private String removeFault;

    /**
     * 机库型号
     */
    private String hiveModel;

    /**
     * 无人车编号
     */
    private String ugvId;

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
        DeviceHive other = (DeviceHive) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getHiveId() == null ? other.getHiveId() == null : this.getHiveId().equals(other.getHiveId()))
            && (this.getHiveName() == null ? other.getHiveName() == null : this.getHiveName().equals(other.getHiveName()))
            && (this.getHiveType() == null ? other.getHiveType() == null : this.getHiveType().equals(other.getHiveType()))
            && (this.getSiteId() == null ? other.getSiteId() == null : this.getSiteId().equals(other.getSiteId()))
            && (this.getHiveFlvUrl() == null ? other.getHiveFlvUrl() == null : this.getHiveFlvUrl().equals(other.getHiveFlvUrl()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
            && (this.getRemoveFault() == null ? other.getRemoveFault() == null : this.getRemoveFault().equals(other.getRemoveFault()))
            && (this.getHiveModel() == null ? other.getHiveModel() == null : this.getHiveModel().equals(other.getHiveModel()))
            && (this.getUgvId() == null ? other.getUgvId() == null : this.getUgvId().equals(other.getUgvId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getHiveId() == null) ? 0 : getHiveId().hashCode());
        result = prime * result + ((getHiveName() == null) ? 0 : getHiveName().hashCode());
        result = prime * result + ((getHiveType() == null) ? 0 : getHiveType().hashCode());
        result = prime * result + ((getSiteId() == null) ? 0 : getSiteId().hashCode());
        result = prime * result + ((getHiveFlvUrl() == null) ? 0 : getHiveFlvUrl().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        result = prime * result + ((getRemoveFault() == null) ? 0 : getRemoveFault().hashCode());
        result = prime * result + ((getHiveModel() == null) ? 0 : getHiveModel().hashCode());
        result = prime * result + ((getUgvId() == null) ? 0 : getUgvId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", hiveId=").append(hiveId);
        sb.append(", hiveName=").append(hiveName);
        sb.append(", hiveType=").append(hiveType);
        sb.append(", siteId=").append(siteId);
        sb.append(", hiveFlvUrl=").append(hiveFlvUrl);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", removeFault=").append(removeFault);
        sb.append(", hiveModel=").append(hiveModel);
        sb.append(", ugvId=").append(ugvId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}