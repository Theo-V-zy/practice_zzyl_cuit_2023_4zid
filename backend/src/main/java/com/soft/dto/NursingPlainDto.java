package com.soft.dto;

import lombok.Data;

import java.util.List;

@Data
public class NursingPlainDto {
    private Integer id;
    private Integer levelId;
    private String levelName;
    private String plainname;
    //某个护理计划下的所有护理项集合
    private List<PlainItemDto> plainItemList;


}
