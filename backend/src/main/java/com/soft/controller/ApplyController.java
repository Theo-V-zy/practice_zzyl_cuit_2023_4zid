package com.soft.controller;

import com.soft.dto.ApplyDto;
import com.soft.pojo.Apply;
import com.soft.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ApplyController {

    @Autowired
    private ApplyService applyService;

    @RequestMapping("/applyPage")
    public Map<String, Object> applyPage(@RequestBody ApplyDto dto) {
        return applyService.queryApplyList(dto);
    }

    @RequestMapping("/saveApply")
    public Map<String, Object> saveApply(@RequestBody Apply apply) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "提交申请失败");

        apply.setApplyTime(new Date());
        apply.setStatus("待审批");
        applyService.save(apply);

        result.put("code", 200);
        result.put("msg", "提交申请成功");
        return result;
    }

    @RequestMapping("/approveApply")
    public Map<String, Object> approveApply(@RequestBody Apply apply) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "审批失败");

        Apply exist = applyService.getById(apply.getId());
        if (exist != null) {
            exist.setStatus(apply.getStatus());
            exist.setApproveUser(apply.getApproveUser());
            exist.setApproveUserId(apply.getApproveUserId());
            exist.setApproveTime(new Date());
            exist.setApproveOpinion(apply.getApproveOpinion());
            applyService.updateById(exist);

            result.put("code", 200);
            result.put("msg", "审批成功");
        }

        return result;
    }

    @RequestMapping("/getApplyById")
    public Map<String, Object> getApplyById(Integer id) {
        Map<String, Object> result = new HashMap<>();
        Apply apply = applyService.getById(id);
        result.put("code", 200);
        result.put("data", apply);
        return result;
    }
}
