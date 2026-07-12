package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.pojo.Apply;

import java.util.Map;

public interface ApplyService extends IService<Apply> {
    Map<String, Object> queryApplyPage(Integer pageNum, Integer pageSize, String applyType, String status);
}
