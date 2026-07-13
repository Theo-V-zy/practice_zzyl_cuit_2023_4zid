package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.CustomerDto;
import com.soft.mapper.CustomerMapper;
import com.soft.pojo.Customer;
import com.soft.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl extends
        ServiceImpl<CustomerMapper, Customer>
    implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public String getMaxCustomerNo() {
        QueryWrapper<Customer> qw = new QueryWrapper<>();
        qw.select("max(customer_no) as customerNo");
        Customer customer = customerMapper.selectOne(qw);
        return customer != null ? customer.getCustomerNo() : null;
    }

    @Override
    public Map<String, Object> queryCustomerList(CustomerDto dto) {
        Page<Customer> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Customer> params = new QueryWrapper<>();

        String name = dto.getName();
        String phone = dto.getPhone();
        String intentionLevel = dto.getIntentionLevel();
        String source = dto.getSource();
        String status = dto.getStatus();

        params.like(!StringUtils.isEmpty(name), "name", name);
        params.like(!StringUtils.isEmpty(phone), "phone", phone);
        params.eq(!StringUtils.isEmpty(intentionLevel), "intention_level", intentionLevel);
        params.eq(!StringUtils.isEmpty(source), "source", source);
        params.eq(!StringUtils.isEmpty(status), "status", status);
        params.orderByDesc("create_time");

        List<Customer> customers = customerMapper.selectList(page, params);

        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("customers", customers);
        return result;
    }
}
