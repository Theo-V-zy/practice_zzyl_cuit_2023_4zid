package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@TableName(value ="t_user")
@Data
public class User implements Serializable {
    @TableId(type=IdType.AUTO)
    private Integer id;
    private String account;
    private String upwd;
    private String realname;
    private String email;
    private String department;
    private String job;
    private String role;
    private String phone;
    private String sex;
    private Integer islock;
    private String image;
    private Integer deptId;
    private Integer postId;
    private Integer roleId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String deptName;
    @TableField(exist = false)
    private String postName;
    @TableField(exist = false)
    private String roleName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
