package com.soft.dto;

import lombok.Data;

@Data
public class UserPageDto {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String realname;
    private String phone;
    private String email;
    private Integer deptId;
    private Integer roleId;
    private Integer status;
}
