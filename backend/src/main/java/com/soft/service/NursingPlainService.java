package com.soft.service;

import com.soft.dto.NursingPlainDto;
import com.soft.dto.NursingPlainPageDto;
import com.soft.pojo.NursingPlain;
import com.soft.pojo.PlainItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author Teacher
* @description 针对表【t_nursing_plain】的数据库操作Service
* @createDate 2026-07-09 14:28:30
*/
public interface NursingPlainService extends IService<NursingPlain> {

    /*实现护理计划的新增*/
    public Map<String,Object>
        saveNursingPlainService(NursingPlainDto nursingPlainDto);

    /*实现护理计划分页查询*/
    public Map<String,Object>
        loadNursingListPageService(NursingPlainPageDto dto);

    /*删除护理计划（级联删除关联项）*/
    public Map<String, Object> deleteNursingPlainService(Integer plainId);

    /*根据护理计划id查询对应的护理项*/
    public List<PlainItem> queryPlainItemsByPlainId(Integer plainId);

    /*更新护理计划状态*/
    public Map<String, Object> updateNursingPlainService(NursingPlain nursingPlain);
}
