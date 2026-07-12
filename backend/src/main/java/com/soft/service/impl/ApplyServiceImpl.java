package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.ApplyDto;
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
public class ApplyServiceImpl extends
        ServiceImpl<ApplyMapper, Apply>
    implements ApplyService {

    @Autowired
    private ApplyMapper applyMapper;

    @Override
    public Map<String, Object> queryApplyList(ApplyDto dto) {
        Page<Apply> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Apply> params = new QueryWrapper<>();

        String applyType = dto.getApplyType();
        String status = dto.getStatus();
        String elderName = dto.getElderName();
        Integer applyUserId = dto.getApplyUserId();

        params.eq(!StringUtils.isEmpty(applyType), "apply_type", applyType);
        params.eq(!StringUtils.isEmpty(status), "status", status);
        params.like(!StringUtils.isEmpty(elderName), "elder_name", elderName);
        params.eq(applyUserId != null, "apply_user_id", applyUserId);
        params.orderByDesc("apply_time");

        List<Apply> applies = applyMapper.selectList(page, params);

        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("applies", applies);
        return result;
    }
}
