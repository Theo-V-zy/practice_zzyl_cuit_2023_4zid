package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.FamilyElderDto;
import com.soft.mapper.FamilyElderMapper;
import com.soft.pojo.FamilyElder;
import com.soft.service.FamilyElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FamilyElderServiceImpl extends
        ServiceImpl<FamilyElderMapper, FamilyElder>
    implements FamilyElderService {

    @Autowired
    private FamilyElderMapper familyElderMapper;

    @Override
    public Map<String, Object> queryFamilyElderList(FamilyElderDto dto) {
        Page<FamilyElder> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<FamilyElder> params = new QueryWrapper<>();

        Integer familyUserId = dto.getFamilyUserId();
        Integer elderId = dto.getElderId();
        String relation = dto.getRelation();
        String status = dto.getStatus();

        params.eq(familyUserId != null, "family_user_id", familyUserId);
        params.eq(elderId != null, "elder_id", elderId);
        params.eq(!StringUtils.isEmpty(relation), "relation", relation);
        params.eq(!StringUtils.isEmpty(status), "status", status);
        params.orderByDesc("bind_time");

        List<FamilyElder> familyElders = familyElderMapper.selectList(page, params);

        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("familyElders", familyElders);
        return result;
    }
}
