package com.soft.dto;

import lombok.Data;

@Data
public class ApplyDto {
    private String applyType;
    private String status;
    private String elderName;
    private Integer applyUserId;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
