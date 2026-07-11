package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.UserDto;
import com.soft.dto.UserLineDto;
import com.soft.dto.UserPwdDto;
import com.soft.mapper.DepartmentMapper;
import com.soft.mapper.PostMapper;
import com.soft.mapper.RoleMapper;
import com.soft.pojo.Department;
import com.soft.pojo.Post;
import com.soft.pojo.Role;
import com.soft.pojo.User;
import com.soft.service.UserService;
import com.soft.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private PostMapper postMapper;

    @Override
    public Map<String, Object> queryUserService(UserDto userDto
            , HttpSession session) {
        Map<String, Object> result=new HashMap<>();
        result.put("code",400);
        String account=userDto.getAccount();
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        // 支持账号或邮箱登录
        wrapper.eq("account",account).or().eq("email",account);
        List<User> users = userMapper.selectList(wrapper);
        if(users==null || users.size()==0){
            result.put("msg",account+"账号不存在......");
            return result;
        }
        User user = users.get(0);
        // 检查是否被禁用
        if (user.getIslock() != null && user.getIslock() == 1) {
            result.put("msg","账号已禁用，请联系管理员");
            return result;
        }
        String dbPwd = user.getUpwd();
        if(!dbPwd.equals(userDto.getUpwd())){
            result.put("msg","输入密码错误......");
            return result;
        }
        // 身份验证通过
        UserLineDto userLineDto=new UserLineDto();
        userLineDto.setId(user.getId());
        userLineDto.setRealname(user.getRealname());
        userLineDto.setAccount(user.getAccount());
        userLineDto.setImage(user.getImage());
        userLineDto.setSex(user.getSex());
        userLineDto.setPhone(user.getPhone());
        userLineDto.setEmail(user.getEmail());

        // 查询角色 → 获取菜单权限
        if (user.getRoleId() != null) {
            Role role = roleMapper.selectById(user.getRoleId());
            if (role != null) {
                userLineDto.setRoleId(role.getId());
                userLineDto.setRoleName(role.getRoleName());
                // 角色状态检查
                if (role.getStatus() != null && role.getStatus() == 0) {
                    result.put("msg","角色已被禁用，请联系管理员");
                    return result;
                }
                // 存入角色的菜单权限
                userLineDto.setMenuIds(role.getMenuIds());
            }
        }
        // 查询部门
        if (user.getDeptId() != null) {
            Department dept = departmentMapper.selectById(user.getDeptId());
            if (dept != null) {
                userLineDto.setDeptId(dept.getId());
                userLineDto.setDeptName(dept.getDeptName());
            }
        }
        // 查询职位
        if (user.getPostId() != null) {
            Post post = postMapper.selectById(user.getPostId());
            if (post != null) {
                userLineDto.setPostId(post.getId());
                userLineDto.setPostName(post.getPostName());
            }
        }

        session.setAttribute("online",userLineDto);
        result.put("code",200);
        return result;
    }

    @Override
    public Map<String, Object> updateUserPwdService(UserPwdDto pwdDto) {
        Map<String,Object> result=new HashMap<>();
        result.put("code",400);
        result.put("msg","更新用户密码失败......");
        Integer id = pwdDto.getId();
        User user = userMapper.selectById(id);
        if(!user.getUpwd().equals(pwdDto.getOldpwd())){
            result.put("msg","原始密码不正确......");
            return result;
        }
        User u=new User();
        u.setId(pwdDto.getId());
        u.setUpwd(pwdDto.getNewpwd());
        userMapper.updateById(u);
        result.put("code",200);
        result.put("msg","更新用户密码成功，请重新登录......");
        return result;
    }
}
