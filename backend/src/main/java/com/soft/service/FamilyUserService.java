package com.soft.service;

import com.soft.pojo.FamilyUser;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;

public interface FamilyUserService extends IService<FamilyUser> {
    Map<String, Object> login(String account, String password);
    Map<String, Object> getMineInfo(Integer familyId);
}
