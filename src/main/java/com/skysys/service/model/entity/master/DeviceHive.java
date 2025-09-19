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
 * @TableName device_hive
 */
// @TableName(value = "device_hive")
@TableName(value = "device_hive_test")
@Data
public class DeviceHive extends BaseModel implements Serializable {
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

}