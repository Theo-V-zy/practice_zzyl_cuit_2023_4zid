package com.soft.controller;

import com.soft.dto.FamilyElderDto;
import com.soft.pojo.Elder;
import com.soft.pojo.FamilyElder;
import com.soft.pojo.FamilyUser;
import com.soft.service.ElderService;
import com.soft.service.FamilyElderService;
import com.soft.mapper.FamilyUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/family")
public class FamilyController {

    @Autowired
    private FamilyElderService familyElderService;

    @Autowired
    private ElderService elderService;

    @Autowired
    private FamilyUserMapper familyUserMapper;

    @RequestMapping("/bindElder")
    public Map<String, Object> bindElder(@RequestBody FamilyElder familyElder) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "绑定失败");

        // Check if elder exists
        Elder elder = elderService.getById(familyElder.getElderId());
        if (elder == null) {
            result.put("msg", "未找到该老人信息");
            return result;
        }

        // Check if already bound
        FamilyElderDto checkDto = new FamilyElderDto();
        checkDto.setFamilyUserId(familyElder.getFamilyUserId());
        checkDto.setElderId(familyElder.getElderId());
        Map<String, Object> existing = familyElderService.queryFamilyElderList(checkDto);
        List<?> existingList = (List<?>) existing.get("familyElders");
        if (existingList != null && !existingList.isEmpty()) {
            result.put("msg", "已绑定该老人，请勿重复绑定");
            return result;
        }

        familyElder.setElderName(elder.getName());
        familyElder.setBindTime(new Date());
        familyElder.setStatus("已绑定");
        familyElderService.save(familyElder);

        result.put("code", 200);
        result.put("msg", "绑定成功");
        return result;
    }

    @RequestMapping("/myElders")
    public Map<String, Object> myElders(@RequestParam Integer familyUserId) {
        Map<String, Object> result = new HashMap<>();

        FamilyElderDto dto = new FamilyElderDto();
        dto.setFamilyUserId(familyUserId);
        dto.setStatus("已绑定");
        dto.setPageSize(100);
        Map<String, Object> pageResult = familyElderService.queryFamilyElderList(dto);

        result.put("code", 200);
        result.put("data", pageResult.get("familyElders"));
        return result;
    }

    @RequestMapping("/unbindElder")
    public Map<String, Object> unbindElder(@RequestParam Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "解绑失败");

        FamilyElder fe = familyElderService.getById(id);
        if (fe != null) {
            fe.setStatus("已解绑");
            familyElderService.updateById(fe);
            result.put("code", 200);
            result.put("msg", "解绑成功");
        }

        return result;
    }

    @RequestMapping("/elderHealthInfo")
    public Map<String, Object> elderHealthInfo(@RequestParam Integer elderId) {
        Map<String, Object> result = new HashMap<>();
        Elder elder = elderService.getById(elderId);
        if (elder != null) {
            result.put("code", 200);
            result.put("data", elder);
        } else {
            result.put("code", 400);
            result.put("msg", "未找到老人信息");
        }
        return result;
    }

    @RequestMapping("/bindByCode")
    public Map<String, Object> bindByCode(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "绑定失败，验证码无效");

        String bindCode = (String) params.get("bindCode");
        Integer familyUserId = (Integer) params.get("familyUserId");

        // Search elder by bind_code in family_elder table
        FamilyElderDto dto = new FamilyElderDto();
        dto.setPageSize(1);
        // Use elder ID as search parameter
        Map<String, Object> searchResult = familyElderService.queryFamilyElderList(dto);
        List<FamilyElder> all = (List<FamilyElder>) searchResult.get("familyElders");

        for (FamilyElder fe : all) {
            if (bindCode != null && bindCode.equals(fe.getBindCode())) {
                // Found matching bind code, create binding for this family user
                FamilyElder newBind = new FamilyElder();
                newBind.setFamilyUserId(familyUserId);
                newBind.setElderId(fe.getElderId());
                newBind.setElderName(fe.getElderName());
                newBind.setRelation((String) params.getOrDefault("relation", "子女"));
                newBind.setBindCode(bindCode);
                newBind.setBindTime(new Date());
                newBind.setStatus("已绑定");
                familyElderService.save(newBind);

                result.put("code", 200);
                result.put("msg", "绑定成功");
                return result;
            }
        }

        return result;
    }
}
