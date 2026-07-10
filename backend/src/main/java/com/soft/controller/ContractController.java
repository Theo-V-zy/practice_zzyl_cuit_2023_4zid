package com.soft.controller;

import com.soft.pojo.Contract;
import com.soft.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class ContractController {

    @Autowired
    private ContractService contractService;

    private static final DateTimeFormatter NO_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @RequestMapping("/contractPage")
    public Map<String, Object> contractPage(@RequestBody Map<String, Object> params) {
        Integer pageNum = (Integer) params.getOrDefault("pageNum", 1);
        Integer pageSize = (Integer) params.getOrDefault("pageSize", 10);
        String status = (String) params.getOrDefault("status", null);
        return contractService.queryContractPage(pageNum, pageSize, status);
    }

    @RequestMapping("/saveContract")
    public Map<String, Object> saveContract(@RequestBody Contract contract) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "添加失败");

        contract.setContractNo("CTR" + LocalDateTime.now().format(NO_FMT) + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        if (contract.getStatus() == null || contract.getStatus().isEmpty()) {
            contract.setStatus("ACTIVE");
        }
        contract.setCreateTime(LocalDateTime.now());
        contractService.save(contract);

        result.put("code", 200);
        result.put("msg", "添加成功");
        return result;
    }

    @RequestMapping("/updateContract")
    public Map<String, Object> updateContract(@RequestBody Contract contract) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "更新失败");
        contractService.updateById(contract);
        result.put("code", 200);
        result.put("msg", "更新成功");
        return result;
    }

    @RequestMapping("/deleteContract")
    public Map<String, Object> deleteContract(Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "删除失败");
        contractService.removeById(id);
        result.put("code", 200);
        result.put("msg", "删除成功");
        return result;
    }
}
