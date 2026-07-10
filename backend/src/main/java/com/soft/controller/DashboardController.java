package com.soft.controller;

import com.soft.dto.UserLineDto;
import com.soft.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class DashboardController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    @RequestMapping("/dashboard/summary")
    public Map<String, Object> summary(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        // 当前登录用户信息
        Object online = session.getAttribute("online");
        if (online == null) {
            result.put("code", 401);
            result.put("msg", "请先登录");
            return result;
        }
        UserLineDto user = (UserLineDto) online;

        Map<String, Object> data = new HashMap<>();
        data.put("userName", user.getRealname());
        data.put("userImage", user.getImage());

        // 统计数据 - 使用真实 SQL 聚合
        try {
            data.put("elderCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM t_elder WHERE status = 'IN'", Integer.class));
            data.put("bedTotal", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM t_bed", Integer.class));
            data.put("bedUsed", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM t_bed WHERE status = 'OCCUPIED'", Integer.class));
            data.put("employeeCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM t_user WHERE islock = 0", Integer.class));
            data.put("monthRevenue", jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(pay_amount), 0) FROM t_order WHERE pay_status = 'PAID' AND DATE_FORMAT(create_time, '%Y%m') = DATE_FORMAT(NOW(), '%Y%m')", Double.class));
            data.put("serviceOrderCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM t_order", Integer.class));
        } catch (Exception e) {
            // 查询失败时使用默认值
            data.putIfAbsent("elderCount", 0);
            data.putIfAbsent("bedTotal", 0);
            data.putIfAbsent("bedUsed", 0);
            data.putIfAbsent("employeeCount", 0);
            data.putIfAbsent("monthRevenue", 0);
            data.putIfAbsent("serviceOrderCount", 0);
        }

        result.put("code", 200);
        result.put("data", data);
        return result;
    }

    @RequestMapping("/dashboard/todo")
    public Map<String, Object> todo(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            // 待处理申请
            List<Map<String, Object>> applies = jdbcTemplate.queryForList(
                "SELECT id, apply_no AS applyNo, apply_type AS applyType, create_time AS createTime, status FROM t_apply WHERE status = 'PENDING' ORDER BY create_time DESC LIMIT 5");
            for (Map<String, Object> item : applies) {
                item.put("todoType", "apply");
                list.add(item);
            }

            // 待处理预约
            List<Map<String, Object>> visits = jdbcTemplate.queryForList(
                "SELECT id, visit_no AS visitNo, visitor_name AS visitorName, appointment_time AS appointmentTime, status FROM t_visit WHERE status = 'PENDING' ORDER BY appointment_time ASC LIMIT 5");
            for (Map<String, Object> item : visits) {
                item.put("todoType", "visit");
                list.add(item);
            }
        } catch (Exception e) {
            // ignore
        }

        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    @RequestMapping("/dashboard/appointments")
    public Map<String, Object> appointments() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            list = jdbcTemplate.queryForList(
                "SELECT v.id, v.visit_no AS visitNo, v.visitor_name AS visitorName, v.visitor_phone AS visitorPhone, v.visit_type AS visitType, v.appointment_time AS appointmentTime, v.status, e.name AS elderName FROM t_visit v LEFT JOIN t_elder e ON v.elder_id = e.id ORDER BY v.appointment_time DESC LIMIT 10");
        } catch (Exception e) {
            // ignore
        }

        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    @RequestMapping("/dashboard/elderStats")
    public Map<String, Object> elderStats() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        try {
            // 按等级统计
            data.put("levelStats", jdbcTemplate.queryForList(
                "SELECT COALESCE(ability_level, '未知') AS name, COUNT(*) AS value FROM t_elder WHERE status = 'IN' GROUP BY ability_level"));
            // 按年龄统计
            data.put("ageStats", jdbcTemplate.queryForList(
                "SELECT CASE WHEN TIMESTAMPDIFF(YEAR, birthday, NOW()) < 60 THEN '60岁以下' WHEN TIMESTAMPDIFF(YEAR, birthday, NOW()) BETWEEN 60 AND 69 THEN '60-69岁' WHEN TIMESTAMPDIFF(YEAR, birthday, NOW()) BETWEEN 70 AND 79 THEN '70-79岁' WHEN TIMESTAMPDIFF(YEAR, birthday, NOW()) BETWEEN 80 AND 89 THEN '80-89岁' ELSE '90岁以上' END AS name, COUNT(*) AS value FROM t_elder WHERE status = 'IN' AND birthday IS NOT NULL GROUP BY name"));
        } catch (Exception e) {
            data.put("levelStats", new ArrayList<>());
            data.put("ageStats", new ArrayList<>());
        }

        result.put("code", 200);
        result.put("data", data);
        return result;
    }

    @RequestMapping("/dashboard/revenueStats")
    public Map<String, Object> revenueStats() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            list = jdbcTemplate.queryForList(
                "SELECT DATE_FORMAT(create_time, '%Y-%m') AS month, COALESCE(SUM(pay_amount), 0) AS revenue, COUNT(*) AS orderCount FROM t_order WHERE pay_status = 'PAID' GROUP BY DATE_FORMAT(create_time, '%Y-%m') ORDER BY month DESC LIMIT 6");
        } catch (Exception e) {
            // ignore
        }

        result.put("code", 200);
        result.put("data", list);
        return result;
    }
}
