package com.soft.dto;

import lombok.Data;

@Data
public class UserLineDto {
    private Integer id;
    private String realname;
    private String sex;
    private String phone;
    private String image;
    private String email;
    private Integer deptId;
    private String deptName;
    private Integer postId;
    private String postName;
    private Integer roleId;
    private String roleName;
    private String account;
    private String menuIds;
}
