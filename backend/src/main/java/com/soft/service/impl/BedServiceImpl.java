package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.BedMapper;
import com.soft.pojo.Bed;
import com.soft.service.BedService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class BedServiceImpl extends ServiceImpl<BedMapper, Bed> implements BedService {

    @Override
    public Map<String, Object> queryBedPage(Integer pageNum, Integer pageSize, String roomNo, String status) {
        Page<Bed> page = new Page<>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10);
        QueryWrapper<Bed> params = new QueryWrapper<>();
        params.like(StringUtils.hasText(roomNo), "room_no", roomNo);
        params.eq(StringUtils.hasText(status), "status", status);
        params.orderByAsc("building", "floor", "room_no", "bed_no");

        page = this.page(page, params);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        return result;
    }
}
