package com.soft.controller;

import com.soft.pojo.Bed;
import com.soft.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BedController {

    @Autowired
    private BedService bedService;

    @RequestMapping("/beds/page")
    public Map<String, Object> page(@RequestBody Map<String, Object> params) {
        Integer pageNum = (Integer) params.getOrDefault("pageNum", 1);
        Integer pageSize = (Integer) params.getOrDefault("pageSize", 10);
        String roomNo = (String) params.getOrDefault("roomNo", null);
        String status = (String) params.getOrDefault("status", null);
        return bedService.queryBedPage(pageNum, pageSize, roomNo, status);
    }

    @PostMapping("/beds")
    public Map<String, Object> add(@RequestBody Bed bed) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (bed.getBedCode() == null || bed.getBedCode().trim().isEmpty()) {
            result.put("msg", "床位编号不能为空");
            return result;
        }
        boolean ok = bedService.save(bed);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "新增成功" : "新增失败");
        return result;
    }

    @RequestMapping(value = "/beds", method = RequestMethod.PUT)
    public Map<String, Object> update(@RequestBody Bed bed) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (bed.getId() == null) {
            result.put("msg", "ID不能为空");
            return result;
        }
        boolean ok = bedService.updateById(bed);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "修改成功" : "修改失败");
        return result;
    }

    @RequestMapping(value = "/beds/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        boolean ok = bedService.removeById(id);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "删除成功" : "删除失败");
        return result;
    }
}
