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
 * 家属端用户
 * @TableName t_family_user
 */
@TableName(value ="t_family_user")
@Data
public class FamilyUser implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String openid;
    private String name;
    private String phone;
    private String avatar;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
