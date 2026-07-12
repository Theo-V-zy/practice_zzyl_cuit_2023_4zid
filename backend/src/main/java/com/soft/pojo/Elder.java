package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@TableName(value = "t_elder")
@Data
public class Elder implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String elderNo;
    private String name;
    private String gender;
    private String idCard;
    private LocalDate birthday;
    private String phone;
    private String avatar;
    private String status;
    private String healthStatus;
    private String abilityLevel;
    private Integer nursingPlainId;
    private String familyContact;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
