package com.skysys.service.model.entity.slave1;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tb_device_hives
 */
@TableName(value ="tb_device_hives")
@Data
public class TbDeviceHives implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 机库编号
     */
    @TableField(value = "hiveID")
    private String hiveID;

    /**
     * 机库名称
     */
    @TableField(value = "hiveName")
    private String hiveName;

    /**
     * 机库类型 1-固定机库 2-移动机库
     */
    @TableField(value = "hiveType")
    private Integer hiveType;

    /**
     * 型号ID，模型表外键
     */
    @TableField(value = "ModelID")
    private Integer modelID;

    /**
     * 是否删除  0已删除 1未删除
     */
    @TableField(value = "isDelete")
    private Integer isDelete;

    /**
     * 机库所属站点编号，站点表外键
     */
    @TableField(value = "siteID")
    private String siteID;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 内窥视角flv
     */
    @TableField(value = "hiveFLVURL")
    private String hiveFLVURL;

    /**
     * [废弃]视频直播通道编号
     */
    @TableField(value = "liveChannelID")
    private Long liveChannelID;

    /**
     * 机库视角配置
     */
    @TableField(value = "viewLiveConfig")
    private Object viewLiveConfig;

    /**
     * 项目编号
     */
    @TableField(value = "PID")
    private String PID;

    /**
     * 状态 1入库 2在役 3故障 4报废
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 企业编号
     */
    @TableField(value = "CPID")
    private String CPID;

    /**
     * 出厂时间
     */
    @TableField(value = "productTime")
    private String productTime;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private String createTime;

    /**
     * 创建人编号
     */
    @TableField(value = "creatorID")
    private Long creatorID;

    /**
     * 创建用户
     */
    @TableField(value = "createUser")
    private String createUser;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private String updateTime;

    /**
     * 更新人编号
     */
    @TableField(value = "updatorID")
    private Long updatorID;

    /**
     * 更新用户
     */
    @TableField(value = "updateUser")
    private String updateUser;

    /**
     * 机库所属绑定站点的位置序列
     */
    @TableField(value = "deviceSiteLocation")
    private Integer deviceSiteLocation;

    /**
     * 设备首次上线时间
     */
    @TableField(value = "firstOnlineTime")
    private String firstOnlineTime;

    /**
     * 设备总在线时长(秒)
     */
    @TableField(value = "totalOnlinTime")
    private Long totalOnlinTime;

    /**
     * 最后一次上线时间
     */
    @TableField(value = "lastOnlineTime")
    private String lastOnlineTime;

    /**
     * 最后一次离线时间
     */
    @TableField(value = "lastOfflineTime")
    private String lastOfflineTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}