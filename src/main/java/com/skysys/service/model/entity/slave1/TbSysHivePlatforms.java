package com.skysys.service.model.entity.slave1;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tb_sys_hive_platforms
 */
@TableName(value ="tb_sys_hive_platforms")
@Data
public class TbSysHivePlatforms implements Serializable {
    /**
     * 
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer ID;

    /**
     * 平台编号
     */
    @TableField(value = "PLATID")
    private Integer PLATID;

    /**
     * 机库编号
     */
    @TableField(value = "hiveID")
    private String hiveID;

    /**
     * 
     */
    @TableField(value = "createUser")
    private String createUser;

    /**
     * 
     */
    @TableField(value = "creatorID")
    private Long creatorID;

    /**
     * 
     */
    @TableField(value = "createTime")
    private String createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}