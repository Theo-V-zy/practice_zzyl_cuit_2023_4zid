package com.soft.dto;

import lombok.Data;

@Data
public class FamilyElderDto {
    private Integer familyUserId;
    private Integer elderId;
    private String relation;
    private String status;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
