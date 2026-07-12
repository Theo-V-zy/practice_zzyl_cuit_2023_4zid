package com.soft.controller;

import com.soft.dto.NursingPlainDto;
import com.soft.dto.NursingPlainPageDto;
import com.soft.pojo.NursingPlain;
import com.soft.pojo.PlainItem;
import com.soft.service.NursingPlainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class NursingPlainItemController {
    //注入service
    @Autowired
    private NursingPlainService nursingPlainService;

    /*定义接口实现护理计划向保存*/
    @RequestMapping("/saveNursingPlain")
    public Map<String,Object> saveNursingPlain(
            @RequestBody NursingPlainDto nursingPlainDto){
        return nursingPlainService.saveNursingPlainService(nursingPlainDto);
    }
    /*定义分页查询接口*/
    @RequestMapping("/pageList")
    public Map<String,Object> pageList(@RequestBody NursingPlainPageDto dto){
        return nursingPlainService.loadNursingListPageService(dto);
    }
    /*根据护理计划id查询对应的护理项*/
    @RequestMapping("/plainItemsByPlainId")
    public List<PlainItem> plainItemsByPlainId(Integer plainId){
        return nursingPlainService.queryPlainItemsByPlainId(plainId);
    }
    /*删除护理计划（级联删除关联项）*/
    @RequestMapping("/deleteNursingPlain")
    public Map<String, Object> deleteNursingPlain(Integer id){
        return nursingPlainService.deleteNursingPlainService(id);
    }
    /*更新护理计划状态*/
    @RequestMapping("/updateNursingPlain")
    public Map<String, Object> updateNursingPlain(@RequestBody NursingPlain nursingPlain){
        return nursingPlainService.updateNursingPlainService(nursingPlain);
    }
}
