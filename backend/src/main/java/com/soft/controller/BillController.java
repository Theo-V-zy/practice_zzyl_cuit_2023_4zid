package com.soft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
public class BillController {

    @Autowired
    private JdbcTemplate jdbc;

    /* 账单分页 */
    @PostMapping("/billPage")
    public Map<String, Object> billPage(@RequestBody Map<String, Object> params) {
        int page = params.containsKey("page") ? (int) params.get("page") : 1;
        int pageSize = params.containsKey("pageSize") ? (int) params.get("pageSize") : 10;
        String elderName = (String) params.getOrDefault("elderName", "");
        String status = (String) params.getOrDefault("status", "");
        int offset = (page - 1) * pageSize;

        StringBuilder where = new StringBuilder("WHERE 1=1");
        List<Object> args = new ArrayList<>();
        if (elderName != null && !elderName.isEmpty()) {
            where.append(" AND b.elder_id IN (SELECT id FROM t_elder WHERE name LIKE ?)");
            args.add("%" + elderName + "%");
        }
        if (status != null && !status.isEmpty()) {
            where.append(" AND b.status=?");
            args.add(status);
        }

        String countSql = "SELECT COUNT(*) FROM t_bill b " + where;
        String dataSql = "SELECT b.*, e.name as elder_name FROM t_bill b LEFT JOIN t_elder e ON b.elder_id=e.id " + where + " ORDER BY b.create_time DESC LIMIT " + offset + "," + pageSize;

        int total = jdbc.queryForObject(countSql, Integer.class, args.toArray());
        List<Map<String, Object>> list = jdbc.queryForList(dataSql, args.toArray());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", list);
        result.put("total", total);
        return result;
    }

    /* 欠费老人 */
    @PostMapping("/arrearsPage")
    public Map<String, Object> arrearsPage(@RequestBody Map<String, Object> params) {
        int page = params.containsKey("page") ? (int) params.get("page") : 1;
        int pageSize = params.containsKey("pageSize") ? (int) params.get("pageSize") : 10;
        String elderName = (String) params.getOrDefault("elderName", "");
        int offset = (page - 1) * pageSize;

        StringBuilder where = new StringBuilder("WHERE b.status='UNPAID' AND b.total_amount > IFNULL(b.paid_amount,0)");
        List<Object> args = new ArrayList<>();
        if (elderName != null && !elderName.isEmpty()) {
            where.append(" AND e.name LIKE ?");
            args.add("%" + elderName + "%");
        }

        String countSql = "SELECT COUNT(*) FROM t_bill b LEFT JOIN t_elder e ON b.elder_id=e.id " + where;
        String dataSql = "SELECT b.*, e.name as elder_name, (b.total_amount - IFNULL(b.paid_amount,0)) as arrears FROM t_bill b LEFT JOIN t_elder e ON b.elder_id=e.id " + where + " ORDER BY arrears DESC LIMIT " + offset + "," + pageSize;

        int total = jdbc.queryForObject(countSql, Integer.class, args.toArray());
        List<Map<String, Object>> list = jdbc.queryForList(dataSql, args.toArray());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", list);
        result.put("total", total);
        return result;
    }

    /* 预缴款充值 */
    @PostMapping("/prepay")
    public Map<String, Object> prepay(@RequestBody Map<String, Object> params) {
        int elderId = Integer.parseInt(params.get("elderId").toString());
        BigDecimal amount = new BigDecimal(params.get("amount").toString());
        String now = java.time.LocalDateTime.now().toString().replace("T", " ").substring(0, 19);

        jdbc.update("INSERT INTO t_bill (bill_no,bill_type,elder_id,total_amount,paid_amount,status,create_time) VALUES (?,?,?,?,?,?,?)",
            "PRE" + System.currentTimeMillis(), "PREPAY", elderId, amount, amount, "PAID", now);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "充值成功");
        return result;
    }

    /* 余额查询 */
    @GetMapping("/balance/{elderId}")
    public Map<String, Object> balance(@PathVariable int elderId) {
        BigDecimal prepay = jdbc.queryForObject(
            "SELECT COALESCE(SUM(total_amount),0) FROM t_bill WHERE elder_id=? AND bill_type='PREPAY' AND status='PAID'",
            BigDecimal.class, elderId);
        BigDecimal charged = jdbc.queryForObject(
            "SELECT COALESCE(SUM(total_amount),0) FROM t_bill WHERE elder_id=? AND bill_type IN('MONTHLY','SERVICE')",
            BigDecimal.class, elderId);
        BigDecimal paid = jdbc.queryForObject(
            "SELECT COALESCE(SUM(paid_amount),0) FROM t_bill WHERE elder_id=?",
            BigDecimal.class, elderId);
        BigDecimal balance = prepay.add(paid).subtract(charged);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        Map<String, Object> data = new HashMap<>();
        data.put("prepay", prepay);
        data.put("charged", charged);
        data.put("paid", paid);
        data.put("balance", balance);
        data.put("elderId", elderId);
        result.put("data", data);
        return result;
    }

    /* 账单详情 */
    @GetMapping("/billInfo")
    public Map<String, Object> billInfo(Integer id) {
        Map<String, Object> bill = jdbc.queryForMap(
            "SELECT b.*, e.name as elder_name FROM t_bill b LEFT JOIN t_elder e ON b.elder_id=e.id WHERE b.id=?", id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", bill);
        return result;
    }
}
