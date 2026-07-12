package com.soft.dto;

import lombok.Data;

@Data
public class OrderDto {
    private String orderNo;
    private String orderStatus;
    private String payStatus;
    private String elderName;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
