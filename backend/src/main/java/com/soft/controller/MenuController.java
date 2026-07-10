package com.soft.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soft.pojo.Menu;
import com.soft.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/sysMenus")
    public List<Menu> sysMenusList(){
        return menuService.querySysMenuList();
    }

    @GetMapping("/menus/tree")
    public Map<String, Object> tree() {
        Map<String, Object> result = new HashMap<>();
        List<Menu> tree = menuService.querySysMenuList();
        result.put("code", 200);
        result.put("data", tree);
        return result;
    }

    @PostMapping("/menus")
    public Map<String, Object> add(@RequestBody Menu menu) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (menu.getMname() == null || menu.getMname().trim().isEmpty()) {
            result.put("msg", "菜单名称不能为空");
            return result;
        }
        if (menu.getPid() == null) menu.setPid(0);
        if (menu.getSort() == null) menu.setSort(1);
        if (menu.getVisible() == null) menu.setVisible(1);
        boolean ok = menuService.save(menu);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "新增成功" : "新增失败");
        return result;
    }

    @RequestMapping(value = "/menus", method = RequestMethod.PUT)
    public Map<String, Object> update(@RequestBody Menu menu) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (menu.getId() == null) {
            result.put("msg", "ID不能为空");
            return result;
        }
        boolean ok = menuService.updateById(menu);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "修改成功" : "修改失败");
        return result;
    }

    @RequestMapping(value = "/menus/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        List<Menu> children = menuService.lambdaQuery().eq(Menu::getPid, id).list();
        if (children != null && !children.isEmpty()) {
            result.put("msg", "存在子菜单，无法删除");
            return result;
        }
        boolean ok = menuService.removeById(id);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "删除成功" : "删除失败");
        return result;
    }

    @PostMapping("/menus/{id}/status")
    public Map<String, Object> updateStatus(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        Integer visible = (Integer) body.get("visible");
        Menu menu = new Menu();
        menu.setId(id);
        menu.setVisible(visible);
        boolean ok = menuService.updateById(menu);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "操作成功" : "操作失败");
        return result;
    }
}
