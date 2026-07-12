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
 * 家属-老人绑定关系
 * @TableName t_family_elder
 */
@TableName(value ="t_family_elder")
@Data
public class FamilyElder implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer familyUserId;
    private Integer elderId;
    private String elderName;
    private String relation;
    private String bindCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date bindTime;
    private String status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
