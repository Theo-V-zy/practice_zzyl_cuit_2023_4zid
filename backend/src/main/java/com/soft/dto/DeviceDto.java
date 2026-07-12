package com.soft.dto;

import lombok.Data;

@Data
public class DeviceDto {
    private String deviceName;
    private String deviceType;
    private String runningStatus;
    private String alarmStatus;
    private String islock;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
