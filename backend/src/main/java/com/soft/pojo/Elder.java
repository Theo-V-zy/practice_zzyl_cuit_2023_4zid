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
 * 老人信息
 * @TableName t_elder
 */
@TableName(value ="t_elder")
@Data
public class Elder implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;
    private Integer age;
    private String gender;
    private String idCard;
    private String phone;
    private Integer bedId;
    private String healthInfo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date checkInTime;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
