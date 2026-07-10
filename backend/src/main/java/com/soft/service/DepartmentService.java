package com.soft.service;

import com.soft.pojo.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface DepartmentService extends IService<Department> {
    List<Department> getTree();
}
