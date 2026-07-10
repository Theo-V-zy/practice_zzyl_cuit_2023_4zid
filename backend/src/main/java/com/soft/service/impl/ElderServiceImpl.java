package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.ElderMapper;
import com.soft.pojo.Elder;
import com.soft.service.ElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ElderServiceImpl extends ServiceImpl<ElderMapper, Elder> implements ElderService {

    @Autowired
    private ElderMapper elderMapper;

    @Override
    public Map<String, Object> queryElderPage(Integer pageNum, Integer pageSize) {
        Page<Elder> page = new Page<>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10);
        List<Elder> elders = elderMapper.selectList(page, null);

        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("elders", elders);
        return result;
    }
}
