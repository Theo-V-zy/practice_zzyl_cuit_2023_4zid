package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.dto.VisitDto;
import com.soft.pojo.Visit;

import java.util.Map;

public interface VisitService extends IService<Visit> {
    Map<String, Object> queryVisitList(VisitDto dto);
}
