package com.soft.controller;

import com.soft.pojo.Department;
import com.soft.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping("/departments/tree")
    public Map<String, Object> tree() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", departmentService.getTree());
        return result;
    }

    @PostMapping("/departments")
    public Map<String, Object> add(@RequestBody Department department) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (department.getDeptName() == null || department.getDeptName().trim().isEmpty()) {
            result.put("msg", "部门名称不能为空");
            return result;
        }
        boolean ok = departmentService.save(department);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "新增成功" : "新增失败");
        return result;
    }

    @RequestMapping(value = "/departments", method = RequestMethod.PUT)
    public Map<String, Object> update(@RequestBody Department department) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (department.getId() == null) {
            result.put("msg", "ID不能为空");
            return result;
        }
        boolean ok = departmentService.updateById(department);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "修改成功" : "修改失败");
        return result;
    }

    @RequestMapping(value = "/departments/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        // 检查是否有子部门
        List<Department> children = departmentService.lambdaQuery().eq(Department::getParentId, id).list();
        if (children != null && !children.isEmpty()) {
            result.put("msg", "存在子部门，无法删除");
            return result;
        }
        boolean ok = departmentService.removeById(id);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "删除成功" : "删除失败");
        return result;
    }
}
