package com.soft.dto;

import lombok.Data;

@Data
public class CustomerDto {
    private String name;
    private String phone;
    private String intentionLevel;
    private String source;
    private String status;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
