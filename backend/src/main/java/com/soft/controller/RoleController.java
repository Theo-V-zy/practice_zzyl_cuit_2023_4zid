package com.soft.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soft.dto.RolePageDto;
import com.soft.pojo.Role;
import com.soft.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/roles/page")
    public Map<String, Object> page(@RequestBody RolePageDto dto) {
        Map<String, Object> result = new HashMap<>();
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(dto.getRoleName())) {
            wrapper.like("role_name", dto.getRoleName());
        }
        if (StringUtils.hasText(dto.getRoleCode())) {
            wrapper.like("role_code", dto.getRoleCode());
        }
        wrapper.orderByDesc("create_time");
        Page<Role> page = roleService.page(new Page<>(dto.getPage(), dto.getPageSize()), wrapper);
        result.put("code", 200);
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        return result;
    }

    @RequestMapping("/roles/list")
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        List<Role> list = roleService.lambdaQuery().eq(Role::getStatus, 1).list();
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    @PostMapping("/roles")
    public Map<String, Object> add(@RequestBody Role role) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (role.getRoleName() == null || role.getRoleName().trim().isEmpty()) {
            result.put("msg", "角色名称不能为空");
            return result;
        }
        boolean ok = roleService.save(role);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "新增成功" : "新增失败");
        return result;
    }

    @RequestMapping(value = "/roles", method = RequestMethod.PUT)
    public Map<String, Object> update(@RequestBody Role role) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (role.getId() == null) {
            result.put("msg", "ID不能为空");
            return result;
        }
        boolean ok = roleService.updateById(role);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "修改成功" : "修改失败");
        return result;
    }

    @RequestMapping(value = "/roles/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        boolean ok = roleService.removeById(id);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "删除成功" : "删除失败");
        return result;
    }

    @PostMapping("/roles/{id}/menus")
    public Map<String, Object> updateMenus(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        Object menuIdsObj = body.get("menuIds");
        String menuIds = menuIdsObj != null ? menuIdsObj.toString() : "";
        Role role = new Role();
        role.setId(id);
        role.setMenuIds(menuIds);
        boolean ok = roleService.updateById(role);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "保存成功" : "保存失败");
        return result;
    }

    @PostMapping("/roles/{id}/dataScope")
    public Map<String, Object> updateDataScope(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        String dataScope = (String) body.get("dataScope");
        Role role = new Role();
        role.setId(id);
        role.setDataScope(dataScope);
        boolean ok = roleService.updateById(role);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "保存成功" : "保存失败");
        return result;
    }
}
