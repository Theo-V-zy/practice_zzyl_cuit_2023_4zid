package com.soft.service;

import com.soft.dto.ElderDto;
import com.soft.pojo.Elder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;

public interface ElderService extends IService<Elder> {
    Map<String, Object> queryElderList(ElderDto dto);
}
