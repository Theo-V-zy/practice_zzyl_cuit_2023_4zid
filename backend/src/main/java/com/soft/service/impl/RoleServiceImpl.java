package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.RoleMapper;
import com.soft.pojo.Role;
import com.soft.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements RoleService {
}
