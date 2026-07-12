package com.soft.controller;

import com.soft.dto.CustomerDto;
import com.soft.pojo.Customer;
import com.soft.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/customerPage")
    public Map<String, Object> customerPage(@RequestBody CustomerDto dto) {
        return customerService.queryCustomerList(dto);
    }

    @RequestMapping("/saveCustomer")
    public Map<String, Object> saveCustomer(@RequestBody Customer customer) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "添加客户失败");

        customer.setCreateTime(new Date());
        customer.setUpdateTime(new Date());
        customerService.save(customer);

        result.put("code", 200);
        result.put("msg", "添加客户成功");
        return result;
    }

    @RequestMapping("/updateCustomer")
    public Map<String, Object> updateCustomer(@RequestBody Customer customer) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "更新客户失败");

        customer.setUpdateTime(new Date());
        customerService.updateById(customer);

        result.put("code", 200);
        result.put("msg", "更新客户成功");
        return result;
    }

    @RequestMapping("/deleteCustomer")
    public Map<String, Object> deleteCustomer(Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "删除客户失败");

        customerService.removeById(id);

        result.put("code", 200);
        result.put("msg", "删除客户成功");
        return result;
    }

    @RequestMapping("/getCustomerById")
    public Map<String, Object> getCustomerById(Integer id) {
        Map<String, Object> result = new HashMap<>();
        Customer customer = customerService.getById(id);
        result.put("code", 200);
        result.put("data", customer);
        return result;
    }
}
