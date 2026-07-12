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
 * 客户管理
 * @TableName t_customer
 */
@TableName(value ="t_customer")
@Data
public class Customer implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;
    private String phone;
    private String intentionLevel;
    private String source;
    private String followUpContent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date nextFollowUpTime;
    private String createUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date updateTime;
    private String islock;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
