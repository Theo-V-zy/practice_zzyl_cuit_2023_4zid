package com.soft.dto;

import lombok.Data;

@Data
public class ElderDto {
    private String name;
    private String status;
    private String gender;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
