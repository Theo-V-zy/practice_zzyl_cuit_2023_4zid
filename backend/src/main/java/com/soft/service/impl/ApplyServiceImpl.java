package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.ApplyMapper;
import com.soft.pojo.Apply;
import com.soft.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Apply> implements ApplyService {

    @Autowired
    private ApplyMapper applyMapper;

    @Override
    public Map<String, Object> queryApplyPage(Integer pageNum, Integer pageSize, String applyType, String status) {
        Page<Apply> page = new Page<>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10);
        QueryWrapper<Apply> params = new QueryWrapper<>();
        params.eq(StringUtils.hasText(applyType), "apply_type", applyType);
        params.eq(StringUtils.hasText(status), "status", status);
        params.orderByDesc("create_time");

        List<Apply> applies = applyMapper.selectList(page, params);

        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("applies", applies);
        return result;
    }
}
