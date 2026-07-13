package com.soft.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soft.dto.UserLineDto;
import com.soft.pojo.Menu;
import com.soft.service.MenuService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/sysMenus")
    public List<Menu> sysMenusList(HttpSession session){
        List<Menu> allMenus = menuService.querySysMenuList();

        // 从Session获取当前用户的菜单权限
        Object online = session.getAttribute("online");
        if (online == null) {
            return allMenus; // 未登录返回全部（登录页用不到）
        }
        UserLineDto user = (UserLineDto) online;
        String menuIds = user.getMenuIds();

        // 如果是全部权限（menuIds 包含所有菜单ID），返回全部
        if (menuIds == null || menuIds.isEmpty() || menuIds.contains("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24")) {
            return allMenus;
        }

        // 解析允许的菜单ID集合
        Set<Integer> allowedIds = Arrays.stream(menuIds.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .collect(Collectors.toSet());

        // 过滤菜单树：保留允许的菜单及其父节点链
        return filterMenus(allMenus, allowedIds);
    }

    private List<Menu> filterMenus(List<Menu> allMenus, Set<Integer> allowedIds) {
        // 1. 先把树拍平成 Map<id, Menu>
        Map<Integer, Menu> flatMap = new HashMap<>();
        flattenTree(allMenus, flatMap);

        // 2. 收集所有需要保留的ID（允许的 + 祖先链）
        Set<Integer> keepIds = new HashSet<>(allowedIds);
        for (Integer id : allowedIds) {
            Menu m = flatMap.get(id);
            while (m != null && m.getPid() != null && m.getPid() != 0) {
                keepIds.add(m.getPid());
                m = flatMap.get(m.getPid());
            }
        }

        // 3. 从原始树中过滤重建（用flatMap找原始节点，而不是在根列表里搜pid）
        List<Menu> filtered = new ArrayList<>();
        for (Menu menu : allMenus) {
            if (keepIds.contains(menu.getId())) {
                Menu copy = copyMenu(menu);
                copy.setSubItems(filterChildren(flatMap.get(menu.getId()), keepIds, flatMap));
                filtered.add(copy);
            }
        }
        return filtered;
    }

    private List<Menu> filterChildren(Menu original, Set<Integer> keepIds, Map<Integer, Menu> flatMap) {
        List<Menu> children = new ArrayList<>();
        if (original == null || original.getSubItems() == null) return children;
        for (Menu child : original.getSubItems()) {
            if (keepIds.contains(child.getId())) {
                Menu copy = copyMenu(child);
                copy.setSubItems(filterChildren(child, keepIds, flatMap));
                children.add(copy);
            }
        }
        return children;
    }

    private void flattenTree(List<Menu> menus, Map<Integer, Menu> flat) {
        for (Menu m : menus) {
            flat.put(m.getId(), m);
            if (m.getSubItems() != null) {
                flattenTree(m.getSubItems(), flat);
            }
        }
    }

    private Menu copyMenu(Menu src) {
        Menu m = new Menu();
        m.setId(src.getId());
        m.setPid(src.getPid());
        m.setMname(src.getMname());
        m.setPath(src.getPath());
        m.setSort(src.getSort());
        m.setVisible(src.getVisible());
        return m;
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
