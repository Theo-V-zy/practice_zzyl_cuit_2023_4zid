package com.soft.service;

import com.soft.dto.ApplyDto;
import com.soft.pojo.Apply;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;

public interface ApplyService extends IService<Apply> {
    Map<String, Object> queryApplyList(ApplyDto dto);
}
