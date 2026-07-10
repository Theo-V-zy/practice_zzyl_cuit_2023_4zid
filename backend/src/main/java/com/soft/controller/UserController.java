package com.soft.controller;

import com.soft.dto.UserDto;
import com.soft.dto.UserLineDto;
import com.soft.dto.UserPwdDto;
import com.soft.pojo.User;
import com.soft.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    //注入service对象
    @Autowired
    private UserService userService;

    /*定义加载用户登录信息接口*/
    @RequestMapping("/loadInfo")
    public UserLineDto loadLoginInfo(HttpSession session){
        Object online = session.getAttribute("online");
        if(online!=null){
            return (UserLineDto) online;
        }
        return null;
    }

    /*定义接口加载用户个人信息*/
    @RequestMapping("/showInfo")
    public User showUserInfo(HttpSession session){
        User user=null;
        Object object = session.getAttribute("online");
        if(object!=null){
            UserLineDto dto= (UserLineDto) object;
            Integer id = dto.getId();
            //查询用户信息
            user = userService.getById(id);
        }

        return user;
    }
    /*定义用户信息更新请求接口*/
    @RequestMapping("/updateUser")
    public Map<String,Object> updateUser(@RequestBody UserLineDto userDto){
        Map<String,Object> result=new HashMap<>();
        result.put("code",400);
        result.put("msg","更新用户信息失败......");
        //更新用户信息
        User user=new User();
        BeanUtils.copyProperties(userDto,user);
        user.setRealname(userDto.getRealname());
        userService.updateById(user);
        result.put("code",200);
        result.put("msg","更新用户信息成功......");
        return result;

    }
    /*定义用户密码更新的接口*/

    @RequestMapping("/updatePwd")
    public Map<String,Object>
            updateUserPwd(@RequestBody UserPwdDto userDto
            ,HttpSession session){
        Object object = session.getAttribute("online");
        if(object!=null){
            UserLineDto dto= (UserLineDto) object;
            Integer id = dto.getId();
            userDto.setId(id); //保存当前登录用户id
        }
        Map<String, Object> result = userService.updateUserPwdService(userDto);
        return result;

    }
}
