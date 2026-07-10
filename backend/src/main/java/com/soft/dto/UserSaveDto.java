package com.soft.dto;

import lombok.Data;

@Data
public class UserSaveDto {
    private Integer id;
    private String account;
    private String upwd;
    private String realname;
    private String email;
    private String phone;
    private String sex;
    private Integer islock;
    private String image;
    private Integer deptId;
    private Integer postId;
    private Integer roleId;
}
