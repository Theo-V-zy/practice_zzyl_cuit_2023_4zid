package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.ElderDto;
import com.soft.mapper.ElderMapper;
import com.soft.pojo.Elder;
import com.soft.service.ElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ElderServiceImpl extends
        ServiceImpl<ElderMapper, Elder>
    implements ElderService {

    @Autowired
    private ElderMapper elderMapper;

    @Override
    public Map<String, Object> queryElderList(ElderDto dto) {
        Page<Elder> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Elder> params = new QueryWrapper<>();

        String name = dto.getName();
        String status = dto.getStatus();
        String gender = dto.getGender();

        params.like(!StringUtils.isEmpty(name), "name", name);
        params.eq(!StringUtils.isEmpty(status), "status", status);
        params.eq(!StringUtils.isEmpty(gender), "gender", gender);
        params.orderByDesc("create_time");

        List<Elder> elders = elderMapper.selectList(page, params);

        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("elders", elders);
        return result;
    }
}
