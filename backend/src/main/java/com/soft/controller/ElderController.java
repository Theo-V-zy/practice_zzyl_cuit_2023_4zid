package com.soft.controller;

import com.soft.dto.ElderDto;
import com.soft.pojo.Elder;
import com.soft.service.ElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ElderController {

    @Autowired
    private ElderService elderService;

    @RequestMapping("/elderPage")
    public Map<String, Object> elderPage(@RequestBody ElderDto dto) {
        return elderService.queryElderList(dto);
    }

    @RequestMapping("/getElderById")
    public Map<String, Object> getElderById(Integer id) {
        Map<String, Object> result = new HashMap<>();
        Elder elder = elderService.getById(id);
        result.put("code", 200);
        result.put("data", elder);
        return result;
    }

    @RequestMapping("/elderList")
    public Map<String, Object> elderList() {
        Map<String, Object> result = new HashMap<>();
        List<Elder> elders = elderService.list();
        result.put("code", 200);
        result.put("data", elders);
        return result;
    }
}
