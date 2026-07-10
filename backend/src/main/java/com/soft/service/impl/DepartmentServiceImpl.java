package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.DepartmentMapper;
import com.soft.pojo.Department;
import com.soft.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
        implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> getTree() {
        List<Department> all = departmentMapper.selectList(new QueryWrapper<Department>().orderByAsc("sort"));
        List<Department> tree = new ArrayList<>();
        for (Department dept : all) {
            if (dept.getParentId() == null || dept.getParentId() == 0) {
                dept.setChildren(getChildren(dept.getId(), all));
                tree.add(dept);
            }
        }
        return tree;
    }

    private List<Department> getChildren(Integer parentId, List<Department> all) {
        List<Department> children = new ArrayList<>();
        for (Department dept : all) {
            if (parentId.equals(dept.getParentId())) {
                dept.setChildren(getChildren(dept.getId(), all));
                children.add(dept);
            }
        }
        return children;
    }
}
