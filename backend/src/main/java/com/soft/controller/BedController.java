package com.soft.controller;

import com.soft.pojo.Bed;
import com.soft.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BedController {

    @Autowired
    private BedService bedService;

    @RequestMapping("/bedPage")
    public Map<String, Object> bedPage(@RequestBody Map<String, Object> params) {
        Integer pageNum = (Integer) params.getOrDefault("pageNum", 1);
        Integer pageSize = (Integer) params.getOrDefault("pageSize", 10);
        String roomNo = (String) params.getOrDefault("roomNo", null);
        String status = (String) params.getOrDefault("status", null);
        return bedService.queryBedPage(pageNum, pageSize, roomNo, status);
    }

    @RequestMapping("/saveBed")
    public Map<String, Object> saveBed(@RequestBody Bed bed) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "添加失败");
        bedService.save(bed);
        result.put("code", 200);
        result.put("msg", "添加成功");
        return result;
    }

    @RequestMapping("/updateBed")
    public Map<String, Object> updateBed(@RequestBody Bed bed) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "更新失败");
        bedService.updateById(bed);
        result.put("code", 200);
        result.put("msg", "更新成功");
        return result;
    }

    @RequestMapping("/deleteBed")
    public Map<String, Object> deleteBed(Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "删除失败");
        bedService.removeById(id);
        result.put("code", 200);
        result.put("msg", "删除成功");
        return result;
    }
}
