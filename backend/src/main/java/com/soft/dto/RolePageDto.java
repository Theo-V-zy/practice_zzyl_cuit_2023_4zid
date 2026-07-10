package com.soft.dto;

import lombok.Data;

@Data
public class RolePageDto {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String roleName;
    private String roleCode;
}
