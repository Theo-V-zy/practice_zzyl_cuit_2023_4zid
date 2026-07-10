package com.soft.controller;

import com.soft.pojo.Apply;
import com.soft.service.ApplyService;
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
public class ApplyController {

    @Autowired
    private ApplyService applyService;

    private static final DateTimeFormatter NO_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @RequestMapping("/applyPage")
    public Map<String, Object> applyPage(@RequestBody Map<String, Object> params) {
        Integer pageNum = (Integer) params.getOrDefault("pageNum", 1);
        Integer pageSize = (Integer) params.getOrDefault("pageSize", 10);
        String applyType = (String) params.getOrDefault("applyType", null);
        String status = (String) params.getOrDefault("status", null);
        return applyService.queryApplyPage(pageNum, pageSize, applyType, status);
    }

    @RequestMapping("/saveApply")
    public Map<String, Object> saveApply(@RequestBody Apply apply) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "添加失败");

        apply.setApplyNo("APL" + LocalDateTime.now().format(NO_FMT) + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        if (apply.getStatus() == null || apply.getStatus().isEmpty()) {
            apply.setStatus("PENDING");
        }
        apply.setCreateTime(LocalDateTime.now());
        applyService.save(apply);

        result.put("code", 200);
        result.put("msg", "添加成功");
        return result;
    }

    @RequestMapping("/updateApply")
    public Map<String, Object> updateApply(@RequestBody Apply apply) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "更新失败");
        apply.setUpdateTime(LocalDateTime.now());
        applyService.updateById(apply);
        result.put("code", 200);
        result.put("msg", "更新成功");
        return result;
    }

    @RequestMapping("/deleteApply")
    public Map<String, Object> deleteApply(Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "删除失败");
        applyService.removeById(id);
        result.put("code", 200);
        result.put("msg", "删除成功");
        return result;
    }

    @RequestMapping("/approveApply")
    public Map<String, Object> approveApply(@RequestBody Apply apply) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "审批失败");

        Apply db = applyService.getById(apply.getId());
        if (db != null) {
            db.setStatus(apply.getStatus());
            db.setApproveUserId(apply.getApproveUserId());
            db.setApproveTime(LocalDateTime.now());
            db.setApproveComment(apply.getApproveComment());
            db.setUpdateTime(LocalDateTime.now());
            applyService.updateById(db);

            result.put("code", 200);
            result.put("msg", "审批成功");
        }
        return result;
    }
}
