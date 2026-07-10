package com.soft.controller;

import com.soft.pojo.Elder;
import com.soft.service.ElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ElderController {

    @Autowired
    private ElderService elderService;

    @RequestMapping("/elders/page")
    public Map<String, Object> page(@RequestBody Map<String, Integer> params) {
        Integer pageNum = params.getOrDefault("pageNum", 1);
        Integer pageSize = params.getOrDefault("pageSize", 10);
        return elderService.queryElderPage(pageNum, pageSize);
    }

    @PostMapping("/elders")
    public Map<String, Object> add(@RequestBody Elder elder) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (elder.getName() == null || elder.getName().trim().isEmpty()) {
            result.put("msg", "老人姓名不能为空");
            return result;
        }
        boolean ok = elderService.save(elder);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "新增成功" : "新增失败");
        return result;
    }

    @RequestMapping(value = "/elders", method = RequestMethod.PUT)
    public Map<String, Object> update(@RequestBody Elder elder) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (elder.getId() == null) {
            result.put("msg", "ID不能为空");
            return result;
        }
        boolean ok = elderService.updateById(elder);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "修改成功" : "修改失败");
        return result;
    }

    @RequestMapping(value = "/elders/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        boolean ok = elderService.removeById(id);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "删除成功" : "删除失败");
        return result;
    }
}
