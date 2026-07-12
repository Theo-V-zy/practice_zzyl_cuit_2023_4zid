package com.soft.dto;

import lombok.Data;

@Data
public class MessageDto {
    private Integer userId;
    private String msgType;
    private Integer isRead;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
