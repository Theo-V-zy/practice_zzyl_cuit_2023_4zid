package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@TableName(value ="t_device")
@Data
public class Device implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String deviceNo;
    private String deviceName;
    private String deviceType;
    private Integer elderId;
    private Integer bedId;
    private String onlineStatus;
    private String lastEventType;
    private String lastEventValue;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date lastEventTime;
    private String alertRule;
    private String alertStatus;
    private String alertContent;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
