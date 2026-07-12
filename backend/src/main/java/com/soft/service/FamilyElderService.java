package com.soft.service;

import com.soft.dto.FamilyElderDto;
import com.soft.pojo.FamilyElder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;

public interface FamilyElderService extends IService<FamilyElder> {
    Map<String, Object> queryFamilyElderList(FamilyElderDto dto);
}
