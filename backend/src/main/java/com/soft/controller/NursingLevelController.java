package com.soft.controller;

import com.soft.pojo.NursingLevel;
import com.soft.service.NursingLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NursingLevelController {

    @Autowired
    private NursingLevelService nursingLevelService;

    /*查询所有护理等级*/
    @RequestMapping("/nursingLevelList")
    public List<NursingLevel> nursingLevelList() {
        return nursingLevelService.list();
    }

    /*新增护理等级*/
    @RequestMapping("/saveNursingLevel")
    public Map<String, Object> saveNursingLevel(@RequestBody NursingLevel nursingLevel) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "添加护理等级失败......");

        nursingLevelService.save(nursingLevel);

        result.put("code", 200);
        result.put("msg", "添加护理等级成功......");

        return result;
    }

    /*更新护理等级*/
    @RequestMapping("/updateNursingLevel")
    public Map<String, Object> updateNursingLevel(@RequestBody NursingLevel nursingLevel) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "更新护理等级失败......");

        nursingLevelService.updateById(nursingLevel);

        result.put("code", 200);
        result.put("msg", "更新护理等级成功......");

        return result;
    }

    /*删除护理等级*/
    @RequestMapping("/deleteNursingLevel")
    public Map<String, Object> deleteNursingLevel(Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "删除护理等级失败......");

        nursingLevelService.removeById(id);

        result.put("code", 200);
        result.put("msg", "删除护理等级成功......");

        return result;
    }
}
