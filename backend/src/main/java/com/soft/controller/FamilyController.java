package com.soft.controller;

import com.soft.pojo.FamilyUser;
import com.soft.service.FamilyUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/family")
public class FamilyController {

    @Autowired
    private FamilyUserService familyUserService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

        Map<String, Object> data = familyUserService.getMineInfo(familyId);
        if ((Integer) data.get("code") == 200) {
            FamilyUser user = (FamilyUser) data.get("data");
            result.put("code", 200);
            result.put("name", user.getName());
            result.put("phone", user.getPhone());
            result.put("avatar", user.getAvatar());

            // 查询绑定的老人
            try {
                List<Map<String, Object>> elders = jdbcTemplate.queryForList(
                    "SELECT e.id, e.name, e.elder_no AS elderNo, e.gender, e.status, e.ability_level AS abilityLevel, " +
                    "fe.relation FROM t_family_elder fe " +
                    "JOIN t_elder e ON fe.elder_id = e.id " +
                    "WHERE fe.family_id = ? AND fe.status = 1", familyId);
                result.put("elders", elders);
            } catch (Exception e) {
                result.put("elders", new ArrayList<>());
            }
        } else {
            result.put("code", 400);
            result.put("msg", "家属不存在");
        }
        return result;
    }

    @PostMapping("/contracts")
    public Map<String, Object> contracts(@RequestBody Map<String, Object> params, HttpSession session) {
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

        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT c.id, c.contract_no AS contractNo, c.contract_no AS contractName, c.start_date AS startDate, " +
                "c.end_date AS endDate, c.monthly_fee AS monthlyFee, c.status, " +
                "c.create_time AS signDate, e.name AS elderName, fu.name AS familyName " +
                "FROM t_contract c " +
                "LEFT JOIN t_elder e ON c.elder_id = e.id " +
                "LEFT JOIN t_family_user fu ON c.family_id = fu.id " +
                "WHERE c.family_id = ? OR c.elder_id IN (SELECT elder_id FROM t_family_elder WHERE family_id = ?) " +
                "ORDER BY c.create_time DESC", familyId, familyId);
            result.put("code", 200);
            result.put("data", list);
            result.put("total", list.size());
        } catch (Exception e) {
            result.put("code", 200);
            result.put("data", new ArrayList<>());
            result.put("total", 0);
        }
        return result;
    }

    @PostMapping("/appointments")
    public Map<String, Object> appointments(@RequestBody Map<String, Object> params, HttpSession session) {
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

        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT v.id, v.visit_no AS visitNo, v.visit_stage AS visitType, v.visitor_name AS visitorName, " +
                "v.visitor_phone AS visitorPhone, v.appointment_time AS appointmentTime, v.appointment_time AS appointmentDate, " +
                "v.status, e.name AS elderName FROM t_visit v " +
                "LEFT JOIN t_elder e ON v.elder_id = e.id " +
                "WHERE v.elder_id IN (SELECT elder_id FROM t_family_elder WHERE family_id = ?) " +
                "ORDER BY v.appointment_time DESC", familyId);
            result.put("code", 200);
            result.put("data", list);
            result.put("total", list.size());
        } catch (Exception e) {
            result.put("code", 200);
            result.put("data", new ArrayList<>());
            result.put("total", 0);
        }
        return result;
    }

    @PostMapping("/orders")
    public Map<String, Object> orders(@RequestBody Map<String, Object> params, HttpSession session) {
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

        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT o.id, o.order_no AS orderNo, o.service_name AS serviceName, o.total_amount AS totalAmount, " +
                "o.pay_amount AS payAmount, o.order_status AS orderStatus, o.create_time AS createTime, o.service_time AS serviceTime, " +
                "e.name AS elderName FROM t_order o " +
                "LEFT JOIN t_elder e ON o.elder_id = e.id " +
                "WHERE o.family_id = ? OR o.elder_id IN (SELECT elder_id FROM t_family_elder WHERE family_id = ?) " +
                "ORDER BY o.create_time DESC", familyId, familyId);
            result.put("code", 200);
            result.put("data", list);
            result.put("total", list.size());
        } catch (Exception e) {
            result.put("code", 200);
            result.put("data", new ArrayList<>());
            result.put("total", 0);
        }
        return result;
    }

    @GetMapping("/orders/{id}")
    public Map<String, Object> orderDetail(@PathVariable Integer id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> detail = jdbcTemplate.queryForMap(
                "SELECT o.id, o.order_no AS orderNo, o.service_name AS serviceName, o.total_amount AS totalAmount, " +
                "o.pay_amount AS payAmount, o.order_status AS orderStatus, o.quantity, " +
                "o.create_time AS createTime, o.service_time AS serviceTime, o.remark, " +
                "e.name AS elderName FROM t_order o " +
                "LEFT JOIN t_elder e ON o.elder_id = e.id " +
                "WHERE o.id = ?", id);
            result.put("code", 200);
            result.put("data", detail);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "订单不存在");
        }
        return result;
    }

    @PostMapping("/bills")
    public Map<String, Object> bills(@RequestBody Map<String, Object> params, HttpSession session) {
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

        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT b.id, b.bill_no AS billNo, b.bill_type AS billType, b.fee_name AS feeName, " +
                "b.bill_month AS billMonth, b.total_amount AS totalAmount, b.paid_amount AS paidAmount, b.status " +
                "FROM t_bill b " +
                "WHERE b.family_id = ? OR b.elder_id IN (SELECT elder_id FROM t_family_elder WHERE family_id = ?) " +
                "ORDER BY b.create_time DESC", familyId, familyId);
            result.put("code", 200);
            result.put("data", list);
            result.put("total", list.size());
        } catch (Exception e) {
            result.put("code", 200);
            result.put("data", new ArrayList<>());
            result.put("total", 0);
        }
        return result;
    }
}
