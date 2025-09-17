package com.skysys.service.model.entity.slave1;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tb_device_uavs
 */
@TableName(value ="tb_device_uavs")
@Data
public class TbDeviceUavs implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 无人机编号
     */
    @TableField(value = "UAVID")
    private String UAVID;

    /**
     * 无人机名称
     */
    @TableField(value = "UAVName")
    private String UAVName;

    /**
     * 模型ID,模型表外键
     */
    @TableField(value = "modelID")
    private Integer modelID;

    /**
     * 无人机飞控SN
     */
    @TableField(value = "FCSN")
    private String FCSN;

    /**
     * 无人机机身SN
     */
    @TableField(value = "SN")
    private String SN;

    /**
     * 站点编号，站点表外键
     */
    @TableField(value = "siteID")
    private String siteID;

    /**
     * 备注信息
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 是否删除  0未删除 1已删除
     */
    @TableField(value = "isDelete")
    private Integer isDelete;

    /**
     * 第一视角flv地址
     */
    @TableField(value = "UAVFLVURL")
    private String UAVFLVURL;

    /**
     * [废弃]视频直播通道编号
     */
    @TableField(value = "liveChannelID")
    private Long liveChannelID;

    /**
     * 无人机视角配置
     */
    @TableField(value = "viewLiveConfig")
    private Object viewLiveConfig;

    /**
     * 版本信息
     */
    @TableField(value = "version")
    private String version;

    /**
     * 飞行总里程
     */
    @TableField(value = "flightMileageSum")
    private Double flightMileageSum;

    /**
     * 飞行总时间
     */
    @TableField(value = "flightTimeSum")
    private Integer flightTimeSum;

    /**
     * 拍照总数量
     */
    @TableField(value = "photoSum")
    private Integer photoSum;

    /**
     * 视频总数量
     */
    @TableField(value = "videoSum")
    private Integer videoSum;

    /**
     * 项目编号
     */
    @TableField(value = "PID")
    private String PID;

    /**
     * 企业编号
     */
    @TableField(value = "CPID")
    private String CPID;

    /**
     * 保险相关信息
     */
    @TableField(value = "insuranceInfo")
    private Object insuranceInfo;

    /**
     * 出厂时间
     */
    @TableField(value = "productTime")
    private String productTime;

    /**
     * 状态   1入库  2在役  3维修 4废弃
     */
    @TableField(value = "status")
    private Integer status;

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
     * 是否配置边缘计算机：1:已配置 2:未配置
     */
    @TableField(value = "isHaveComputer")
    private Integer isHaveComputer;

    /**
     * 边缘计算机ID
     */
    @TableField(value = "ECID")
    private String ECID;

    /**
     * 无人机所属绑定站点的位置序列
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