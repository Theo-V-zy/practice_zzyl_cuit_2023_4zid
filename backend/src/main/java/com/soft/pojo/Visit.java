package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@TableName(value = "t_visit")
@Data
public class Visit implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String visitNo;
    private String visitStage;
    private String visitorName;
    private String visitorPhone;
    private String visitType;
    private Integer elderId;
    private String elderName;
    private LocalDateTime appointmentTime;
    private LocalDateTime arriveTime;
    private LocalDateTime leaveTime;
    private String status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
