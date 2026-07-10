package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.VisitDto;
import com.soft.mapper.VisitMapper;
import com.soft.pojo.Visit;
import com.soft.service.VisitService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class VisitServiceImpl extends ServiceImpl<VisitMapper, Visit> implements VisitService {

    @Override
    public Map<String, Object> queryVisitList(VisitDto dto) {
        Integer pageNum = dto.getPageNum() != null ? dto.getPageNum() : 1;
        Integer pageSize = dto.getPageSize() != null ? dto.getPageSize() : 10;
        Page<Visit> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Visit> params = new QueryWrapper<>();

        params.like(StringUtils.hasText(dto.getVisitorName()), "visitor_name", dto.getVisitorName());
        params.eq(StringUtils.hasText(dto.getVisitorPhone()), "visitor_phone", dto.getVisitorPhone());
        params.eq(StringUtils.hasText(dto.getVisitStage()), "visit_stage", dto.getVisitStage());
        params.eq(StringUtils.hasText(dto.getStatus()), "status", dto.getStatus());
        params.orderByDesc("create_time");

        page = this.page(page, params);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        return result;
    }
}
