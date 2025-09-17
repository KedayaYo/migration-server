package com.skysys.service.model.entity.slave1;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tb_sys_device_models
 */
@TableName(value ="tb_sys_device_models")
@Data
public class TbSysDeviceModels implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 型号名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 模型编号
     */
    @TableField(value = "modelCode")
    private String modelCode;

    /**
     * 生产厂家
     */
    @TableField(value = "brand")
    private String brand;

    /**
     * 外形图预览url
     */
    @TableField(value = "appearanceUrl")
    private String appearanceUrl;

    /**
     * 类型 1无人机  2机库 3相机
     */
    @TableField(value = "modelType")
    private Integer modelType;

    /**
     * 模型属性信息
     */
    @TableField(value = "modelProperty")
    private Object modelProperty;

    /**
     * 状态信息 1启用 0禁用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

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
     * 无人机最小飞行速度
     */
    @TableField(value = "uavMinFlightSpeed")
    private Double uavMinFlightSpeed;

    /**
     * 无人机最大飞行速度
     */
    @TableField(value = "uavMaxFlightSpeed")
    private Double uavMaxFlightSpeed;

    /**
     * 无人机最大飞行高度
     */
    @TableField(value = "uavMaxFlightHeight")
    private Double uavMaxFlightHeight;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}