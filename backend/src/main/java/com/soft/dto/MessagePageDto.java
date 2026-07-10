package com.soft.dto;

import lombok.Data;

@Data
public class MessagePageDto {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String title;
    private String messageType;
    private Integer readStatus;
    private String startTime;
    private String endTime;
}
