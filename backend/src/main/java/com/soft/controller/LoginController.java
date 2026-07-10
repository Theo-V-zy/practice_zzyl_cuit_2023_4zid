package com.soft.controller;

import com.soft.dto.UserDto;
import com.soft.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {



    //注入service对象
    @Autowired
    private UserService userService;
    /*定义用户身份验证接口*/
    @RequestMapping("/login")
    public Map<String,Object> userLogin(
            @RequestBody UserDto userDto

            , HttpSession session){
        return userService.queryUserService(userDto,session);
    }
}
