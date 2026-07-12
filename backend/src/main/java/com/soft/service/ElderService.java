package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.pojo.Elder;

import java.util.Map;

public interface ElderService extends IService<Elder> {
    Map<String, Object> queryElderPage(Integer pageNum, Integer pageSize);
}
