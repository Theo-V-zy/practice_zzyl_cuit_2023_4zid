package com.soft.controller;

import com.soft.pojo.Contract;
import com.soft.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/contracts/page")
    public Map<String, Object> page(@RequestBody Map<String, Object> params) {
        Integer pageNum = (Integer) params.getOrDefault("pageNum", 1);
        Integer pageSize = (Integer) params.getOrDefault("pageSize", 10);
        String status = (String) params.getOrDefault("status", null);
        return contractService.queryContractPage(pageNum, pageSize, status);
    }

    @PostMapping("/contracts")
    public Map<String, Object> add(@RequestBody Contract contract) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (contract.getElderId() == null) {
            result.put("msg", "请选择老人");
            return result;
        }

        contract.setContractNo("CTR" + LocalDateTime.now().format(NO_FMT) + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        if (contract.getStatus() == null || contract.getStatus().isEmpty()) {
            contract.setStatus("ACTIVE");
        }
        contract.setCreateTime(LocalDateTime.now());

        boolean ok = contractService.save(contract);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "新增成功" : "新增失败");
        return result;
    }

    @RequestMapping(value = "/contracts", method = RequestMethod.PUT)
    public Map<String, Object> update(@RequestBody Contract contract) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (contract.getId() == null) {
            result.put("msg", "ID不能为空");
            return result;
        }
        boolean ok = contractService.updateById(contract);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "修改成功" : "修改失败");
        return result;
    }

    @RequestMapping(value = "/contracts/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        boolean ok = contractService.removeById(id);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "删除成功" : "删除失败");
        return result;
    }
}
