package com.soft.dto;

import lombok.Data;

@Data
public class VisitDto {
    private String visitorName;
    private String visitorPhone;
    private String visitStage;
    private String status;
    private Integer pageNum;
    private Integer pageSize;
}
