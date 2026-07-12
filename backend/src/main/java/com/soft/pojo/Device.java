package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 智能监测设备
 * @TableName t_device
 */
@TableName(value ="t_device")
@Data
public class Device implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String deviceName;
    private String deviceType;
    private String deviceCode;
    private Integer elderId;
    private String elderName;
    private Integer bedId;
    private String installLocation;
    private String runningStatus;
    private String alarmStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date lastHeartbeat;
    private String islock;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
