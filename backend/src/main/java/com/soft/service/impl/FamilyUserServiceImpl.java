package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.FamilyUserMapper;
import com.soft.pojo.FamilyUser;
import com.soft.service.FamilyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FamilyUserServiceImpl extends ServiceImpl<FamilyUserMapper, FamilyUser>
        implements FamilyUserService {

    @Autowired
    private FamilyUserMapper familyUserMapper;

    @Override
    public Map<String, Object> login(String account, String password) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);

        QueryWrapper<FamilyUser> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account);
        FamilyUser user = familyUserMapper.selectOne(wrapper);

        if (user == null) {
            result.put("msg", "账号不存在");
            return result;
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            result.put("msg", "账号已被禁用");
            return result;
        }
        if (!password.equals(user.getPassword())) {
            result.put("msg", "密码错误");
            return result;
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("name", user.getName());
        userInfo.put("phone", user.getPhone());
        userInfo.put("avatar", user.getAvatar());
        result.put("data", userInfo);
        result.put("code", 200);
        result.put("msg", "登录成功");
        return result;
    }

    @Override
    public Map<String, Object> getMineInfo(Integer familyId) {
        Map<String, Object> result = new HashMap<>();
        FamilyUser user = familyUserMapper.selectById(familyId);
        if (user == null) {
            result.put("code", 400);
            result.put("msg", "家属不存在");
            return result;
        }
        result.put("code", 200);
        result.put("data", user);
        return result;
    }
}
