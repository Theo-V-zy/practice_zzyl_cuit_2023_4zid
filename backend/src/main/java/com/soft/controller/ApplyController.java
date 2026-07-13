package com.soft.controller;

import com.soft.pojo.Apply;
import com.soft.service.ApplyService;
import com.soft.service.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/applies/page")
    public Map<String, Object> page(@RequestBody Map<String, Object> params) {
        Integer pageNum = toInt(params.get("pageNum"), 1);
        Integer pageSize = toInt(params.get("pageSize"), 10);
        String applyType = toString(params.get("applyType"));
        String status = toString(params.get("status"));
        String elderName = toString(params.get("elderName"));
        Integer applyUserId = toInt(params.get("applyUserId"), null);
        return applyService.queryApplyPage(pageNum, pageSize, applyType, status, elderName, applyUserId);
    }

    private Integer toInt(Object val, Integer defaultVal) {
        if (val == null || "".equals(val)) return defaultVal;
        return Integer.valueOf(String.valueOf(val));
    }

    private String toString(Object val) {
        if (val == null || "".equals(val)) return null;
        String s = String.valueOf(val).trim();
        return s.isEmpty() ? null : s;
    }

    @PostMapping("/applies")
    public Map<String, Object> add(@RequestBody Apply apply) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (apply.getElderId() == null) {
            result.put("msg", "请选择老人");
            return result;
        }
        if (apply.getApplyType() == null || apply.getApplyType().trim().isEmpty()) {
            result.put("msg", "申请类型不能为空");
            return result;
        }

        apply.setApplyNo("APL" + LocalDateTime.now().format(NO_FMT) + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        if (apply.getStatus() == null || apply.getStatus().isEmpty()) {
            apply.setStatus("PENDING");
        }
        apply.setCreateTime(LocalDateTime.now());

        boolean ok = applyService.save(apply);
        if (ok) {
            String typeName = apply.getApplyType().equals("CHECKIN") ? "入住" : apply.getApplyType().equals("CHECKOUT") ? "退住" : "请假";
            MessageUtil.send("【待办事项】新的" + typeName + "申请", "用户提交了" + typeName + "申请，请尽快审批。", "业务提醒", "APPLY");
        }
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "新增成功" : "新增失败");
        return result;
    }

    @RequestMapping(value = "/applies", method = RequestMethod.PUT)
    public Map<String, Object> update(@RequestBody Apply apply) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (apply.getId() == null) {
            result.put("msg", "ID不能为空");
            return result;
        }
        apply.setUpdateTime(LocalDateTime.now());
        boolean ok = applyService.updateById(apply);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "修改成功" : "修改失败");
        return result;
    }

    @RequestMapping(value = "/applies/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        boolean ok = applyService.removeById(id);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "删除成功" : "删除失败");
        return result;
    }

    @RequestMapping("/applies/approve")
    public Map<String, Object> approve(@RequestBody Apply apply) {
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
            String typeName = db.getApplyType().equals("CHECKIN") ? "入住" : db.getApplyType().equals("CHECKOUT") ? "退住" : "请假";
            String resultName = "APPROVED".equals(db.getStatus()) ? "通过" : "驳回";
            MessageUtil.send("【审批通知】" + typeName + "申请已" + resultName, "您的" + typeName + "申请已被" + resultName + "。", "业务提醒", "APPLY");
        }
        return result;
    }
}
