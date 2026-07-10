package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.ElderMapper;
import com.soft.pojo.Elder;
import com.soft.service.ElderService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ElderServiceImpl extends ServiceImpl<ElderMapper, Elder> implements ElderService {

    @Override
    public Map<String, Object> queryElderPage(Integer pageNum, Integer pageSize) {
        Page<Elder> page = new Page<>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10);
        page = this.page(page);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        return result;
    }
}
