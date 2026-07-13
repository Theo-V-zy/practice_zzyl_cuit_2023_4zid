package com.soft.service;

import com.soft.dto.CustomerDto;
import com.soft.pojo.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;

public interface CustomerService extends IService<Customer> {
    Map<String, Object> queryCustomerList(CustomerDto dto);
    String getMaxCustomerNo();
}
