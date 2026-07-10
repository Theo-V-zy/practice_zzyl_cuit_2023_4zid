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
        // 收集所有需要保留的ID（包括允许菜单的所有祖先）
        Set<Integer> keepIds = new HashSet<>(allowedIds);
        for (Menu menu : allMenus) {
            if (allowedIds.contains(menu.getId())) {
                // 向上追溯父节点
                addParentChain(allMenus, menu.getPid(), keepIds);
            }
        }

        // 过滤并重建树
        List<Menu> filtered = new ArrayList<>();
        for (Menu menu : allMenus) {
            if (keepIds.contains(menu.getId()) && menu.getPid() == 0) {
                Menu copy = copyMenu(menu);
                copy.setSubItems(filterChildren(allMenus, keepIds, menu.getId()));
                filtered.add(copy);
            }
        }
        return filtered;
    }

    private void addParentChain(List<Menu> allMenus, Integer pid, Set<Integer> keepIds) {
        if (pid == null || pid == 0) return;
        keepIds.add(pid);
        for (Menu m : allMenus) {
            if (m.getId().equals(pid)) {
                addParentChain(allMenus, m.getPid(), keepIds);
                break;
            }
        }
    }

    private List<Menu> filterChildren(List<Menu> allMenus, Set<Integer> keepIds, Integer parentId) {
        List<Menu> children = new ArrayList<>();
        for (Menu menu : allMenus) {
            if (keepIds.contains(menu.getId()) && parentId.equals(menu.getPid())) {
                Menu copy = copyMenu(menu);
                copy.setSubItems(filterChildren(allMenus, keepIds, menu.getId()));
                children.add(copy);
            }
        }
        return children;
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
