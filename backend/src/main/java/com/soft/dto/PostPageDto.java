package com.soft.dto;

import lombok.Data;

@Data
public class PostPageDto {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String postName;
    private Integer status;
}
