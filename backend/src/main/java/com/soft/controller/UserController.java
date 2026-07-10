package com.soft.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soft.dto.*;
import com.soft.pojo.Department;
import com.soft.pojo.Post;
import com.soft.pojo.Role;
import com.soft.pojo.User;
import com.soft.service.DepartmentService;
import com.soft.service.PostService;
import com.soft.service.RoleService;
import com.soft.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PostService postService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/loadInfo")
    public UserLineDto loadLoginInfo(HttpSession session){
        Object online = session.getAttribute("online");
        if(online!=null){
            return (UserLineDto) online;
        }
        return null;
    }

    @RequestMapping("/showInfo")
    public User showUserInfo(HttpSession session){
        User user = null;
        Object object = session.getAttribute("online");
        if(object != null){
            UserLineDto dto = (UserLineDto) object;
            Integer id = dto.getId();
            user = userService.getById(id);
        }
        return user;
    }

    @RequestMapping("/updateUser")
    public Map<String,Object> updateUser(@RequestBody UserLineDto userDto, HttpSession session){
        Map<String,Object> result = new HashMap<>();
        result.put("code",400);
        result.put("msg","更新用户信息失败......");

        Object online = session.getAttribute("online");
        if(online == null){
            result.put("msg","请先登录");
            return result;
        }
        UserLineDto currentUser = (UserLineDto) online;
        User user = new User();
        user.setId(currentUser.getId());
        user.setRealname(userDto.getRealname());
        user.setSex(userDto.getSex());
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        user.setImage(userDto.getImage());
        userService.updateById(user);

        // 更新session中的用户信息
        if(StringUtils.hasText(userDto.getRealname())){
            currentUser.setRealname(userDto.getRealname());
        }
        currentUser.setImage(userDto.getImage());
        session.setAttribute("online", currentUser);

        result.put("code",200);
        result.put("msg","更新用户信息成功......");
        return result;
    }

    @RequestMapping("/updatePwd")
    public Map<String,Object> updateUserPwd(@RequestBody UserPwdDto userDto, HttpSession session){
        Object object = session.getAttribute("online");
        if(object != null){
            UserLineDto dto = (UserLineDto) object;
            Integer id = dto.getId();
            userDto.setId(id);
        }
        return userService.updateUserPwdService(userDto);
    }

    // ========== 用户管理 CRUD ==========

    @RequestMapping("/users/page")
    public Map<String, Object> userPage(@RequestBody UserPageDto dto) {
        Map<String, Object> result = new HashMap<>();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(dto.getRealname())) {
            wrapper.like("realname", dto.getRealname());
        }
        if (StringUtils.hasText(dto.getPhone())) {
            wrapper.like("phone", dto.getPhone());
        }
        if (dto.getDeptId() != null) {
            wrapper.eq("dept_id", dto.getDeptId());
        }
        if (dto.getRoleId() != null) {
            wrapper.eq("role_id", dto.getRoleId());
        }
        if (dto.getStatus() != null) {
            wrapper.eq("islock", dto.getStatus() == 1 ? 0 : 1);
        }
        wrapper.orderByDesc("create_time");
        Page<User> page = userService.page(new Page<>(dto.getPage(), dto.getPageSize()), wrapper);

        // 填充部门、职位、角色名称
        for (User user : page.getRecords()) {
            if (user.getDeptId() != null) {
                Department dept = departmentService.getById(user.getDeptId());
                if (dept != null) user.setDeptName(dept.getDeptName());
            }
            if (user.getPostId() != null) {
                Post post = postService.getById(user.getPostId());
                if (post != null) user.setPostName(post.getPostName());
            }
            if (user.getRoleId() != null) {
                Role role = roleService.getById(user.getRoleId());
                if (role != null) user.setRoleName(role.getRoleName());
            }
        }

        result.put("code", 200);
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        return result;
    }

    @PostMapping("/users")
    public Map<String, Object> addUser(@RequestBody UserSaveDto dto) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);

        if (!StringUtils.hasText(dto.getAccount())) {
            result.put("msg", "账号不能为空");
            return result;
        }
        if (!StringUtils.hasText(dto.getRealname())) {
            result.put("msg", "姓名不能为空");
            return result;
        }

        // 检查账号是否已存在
        User exist = userService.lambdaQuery().eq(User::getAccount, dto.getAccount()).one();
        if (exist != null) {
            result.put("msg", "账号已存在");
            return result;
        }

        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setIslock(dto.getIslock() != null ? dto.getIslock() : 0);
        user.setUpwd("123456"); // 默认密码

        boolean ok = userService.save(user);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "新增成功" : "新增失败");
        return result;
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    public Map<String, Object> updateUserInfo(@RequestBody UserSaveDto dto) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);

        if (dto.getId() == null) {
            result.put("msg", "ID不能为空");
            return result;
        }

        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setUpwd(null); // 编辑不修改密码
        boolean ok = userService.updateById(user);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "修改成功" : "修改失败");
        return result;
    }

    @PostMapping("/users/{id}/status")
    public Map<String, Object> updateUserStatus(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        Integer status = (Integer) body.get("status");
        User user = new User();
        user.setId(id);
        user.setIslock(status == 1 ? 0 : 1); // status=1启用时islock=0
        boolean ok = userService.updateById(user);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "操作成功" : "操作失败");
        return result;
    }

    @PostMapping("/users/{id}/resetPassword")
    public Map<String, Object> resetPassword(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        User user = new User();
        user.setId(id);
        user.setUpwd("123456");
        boolean ok = userService.updateById(user);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "密码已重置为123456" : "重置失败");
        return result;
    }

    @PostMapping("/users/{id}/roles")
    public Map<String, Object> assignRoles(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        Object roleIdsObj = body.get("roleIds");
        User user = new User();
        user.setId(id);
        if (roleIdsObj instanceof List) {
            @SuppressWarnings("unchecked")
            List<Integer> roleIds = (List<Integer>) roleIdsObj;
            if (!roleIds.isEmpty()) {
                user.setRoleId(roleIds.get(0)); // 只取第一个角色ID
            }
        }
        boolean ok = userService.updateById(user);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "分配成功" : "分配失败");
        return result;
    }
}
