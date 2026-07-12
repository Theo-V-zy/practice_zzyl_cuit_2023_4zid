package com.soft.dto;

import lombok.Data;

@Data
public class DeviceDto {
    private String deviceName;
    private String deviceType;
    private String onlineStatus;
    private String alertStatus;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
