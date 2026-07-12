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
 * 协同工作/审批申请
 * @TableName t_apply
 */
@TableName(value ="t_apply")
@Data
public class Apply implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String applyType;
    private String applyUser;
    private Integer applyUserId;
    private Integer elderId;
    private String elderName;
    private String description;
    private String status;
    private String approveUser;
    private Integer approveUserId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date approveTime;
    private String approveOpinion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date applyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
