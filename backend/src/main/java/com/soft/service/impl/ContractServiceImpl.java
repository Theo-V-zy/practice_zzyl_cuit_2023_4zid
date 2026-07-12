package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.ContractMapper;
import com.soft.pojo.Contract;
import com.soft.service.ContractService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {

    @Override
    public Map<String, Object> queryContractPage(Integer pageNum, Integer pageSize, String status) {
        Page<Contract> page = new Page<>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10);
        QueryWrapper<Contract> params = new QueryWrapper<>();
        params.eq(StringUtils.hasText(status), "status", status);
        params.orderByDesc("create_time");

        page = this.page(page, params);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        return result;
    }
}
