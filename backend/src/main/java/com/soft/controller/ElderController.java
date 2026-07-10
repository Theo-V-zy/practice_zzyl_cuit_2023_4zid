package com.soft.controller;

import com.soft.pojo.Elder;
import com.soft.service.ElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ElderController {

    @Autowired
    private ElderService elderService;

    @RequestMapping("/elderPage")
    public Map<String, Object> elderPage(@RequestBody Map<String, Integer> params) {
        Integer pageNum = params.getOrDefault("pageNum", 1);
        Integer pageSize = params.getOrDefault("pageSize", 10);
        return elderService.queryElderPage(pageNum, pageSize);
    }

    @RequestMapping("/saveElder")
    public Map<String, Object> saveElder(@RequestBody Elder elder) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "添加失败");
        elderService.save(elder);
        result.put("code", 200);
        result.put("msg", "添加成功");
        return result;
    }

    @RequestMapping("/updateElder")
    public Map<String, Object> updateElder(@RequestBody Elder elder) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "更新失败");
        elderService.updateById(elder);
        result.put("code", 200);
        result.put("msg", "更新成功");
        return result;
    }

    @RequestMapping("/deleteElder")
    public Map<String, Object> deleteElder(Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "删除失败");
        elderService.removeById(id);
        result.put("code", 200);
        result.put("msg", "删除成功");
        return result;
    }
}
