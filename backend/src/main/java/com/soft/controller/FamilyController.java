package com.soft.controller;

import com.soft.dto.UserLineDto;
import com.soft.pojo.FamilyUser;
import com.soft.service.FamilyUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/family")
public class FamilyController {

    @Autowired
    private FamilyUserService familyUserService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body, HttpSession session) {
        String account = body.get("account");
        String password = body.get("password");
        Map<String, Object> result = familyUserService.login(account, password);
        if ((Integer) result.get("code") == 200) {
            @SuppressWarnings("unchecked")
            Map<String, Object> userInfo = (Map<String, Object>) result.get("data");
            session.setAttribute("familyOnline", userInfo);
        }
        return result;
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {
        session.removeAttribute("familyOnline");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "退出成功");
        return result;
    }

    @GetMapping("/profile")
    public Map<String, Object> profile(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Object online = session.getAttribute("familyOnline");
        if (online == null) {
            result.put("code", 401);
            result.put("msg", "请先登录");
            return result;
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> userInfo = (Map<String, Object>) online;
        result.put("code", 200);
        result.putAll(userInfo);
        return result;
    }

    @GetMapping("/mine")
    public Map<String, Object> mine(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Object online = session.getAttribute("familyOnline");
        if (online == null) {
            result.put("code", 401);
            result.put("msg", "请先登录");
            return result;
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> userInfo = (Map<String, Object>) online;
        Integer familyId = (Integer) userInfo.get("id");
        return familyUserService.getMineInfo(familyId);
    }

    @PostMapping("/contracts")
    public Map<String, Object> contracts(@RequestBody Map<String, Object> params, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", new java.util.ArrayList<>());
        result.put("total", 0);
        result.put("msg", "合同数据由组员A完成后端对接");
        return result;
    }

    @PostMapping("/appointments")
    public Map<String, Object> appointments(@RequestBody Map<String, Object> params, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", new java.util.ArrayList<>());
        result.put("total", 0);
        result.put("msg", "预约数据由组员A完成后端对接");
        return result;
    }

    @PostMapping("/orders")
    public Map<String, Object> orders(@RequestBody Map<String, Object> params, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", new java.util.ArrayList<>());
        result.put("total", 0);
        result.put("msg", "订单数据由组员B完成后端对接");
        return result;
    }

    @GetMapping("/orders/{id}")
    public Map<String, Object> orderDetail(@PathVariable Integer id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", new HashMap<>());
        result.put("msg", "订单详情由组员B完成后端对接");
        return result;
    }

    @PostMapping("/bills")
    public Map<String, Object> bills(@RequestBody Map<String, Object> params, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", new java.util.ArrayList<>());
        result.put("total", 0);
        result.put("msg", "账单数据由组员B完成后端对接");
        return result;
    }
}
