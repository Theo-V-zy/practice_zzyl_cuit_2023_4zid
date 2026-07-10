package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@TableName(value = "t_apply")
@Data
public class Apply implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String applyNo;
    private String applyType;
    private Integer elderId;
    private Integer familyId;
    private Integer applyUserId;
    private String currentStep;
    private String reason;
    private String status;
    private Integer approveUserId;
    private LocalDateTime approveTime;
    private String approveComment;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String elderName;
    @TableField(exist = false)
    private String applyUserName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
