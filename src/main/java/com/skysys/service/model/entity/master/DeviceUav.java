package com.skysys.service.model.entity.master;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.skysys.service.model.entity.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @TableName device_uav
 */
// @TableName(value = "device_uav")
@TableName(value = "device_uav_test")
@Data
public class DeviceUav extends BaseModel implements Serializable {
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
     * 删除标记
     */
    private Integer deleted;

    /**
     * 无人机当前的实时位置-[经度，纬度] -WGS84
     */
    private String uavLocation;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}