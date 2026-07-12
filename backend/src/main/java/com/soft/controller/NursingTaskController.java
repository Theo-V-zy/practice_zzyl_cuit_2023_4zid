package com.soft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class NursingTaskController {

    @Autowired
    private JdbcTemplate jdbc;

    /* 任务分页 */
    @PostMapping("/nursingTaskPage")
    public Map<String, Object> taskPage(@RequestBody Map<String, Object> params) {
        int page = (int) params.getOrDefault("page", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        String elderName = (String) params.getOrDefault("elderName", "");
        String status = (String) params.getOrDefault("status", "");
        int offset = (page - 1) * pageSize;

        StringBuilder where = new StringBuilder("WHERE 1=1");
        List<Object> args = new ArrayList<>();
        if (!elderName.isEmpty()) { where.append(" AND e.name LIKE ?"); args.add("%" + elderName + "%"); }
        if (!status.isEmpty()) { where.append(" AND t.status=?"); args.add(status); }

        String sql = "SELECT t.*, e.name as elder_name, ni.itemname as item_name FROM t_nursing_task t LEFT JOIN t_elder e ON t.elder_id=e.id LEFT JOIN t_nursimg_item ni ON t.item_id=ni.id " + where + " ORDER BY t.create_time DESC LIMIT " + offset + "," + pageSize;
        String cnt = "SELECT COUNT(*) FROM t_nursing_task t LEFT JOIN t_elder e ON t.elder_id=e.id " + where;

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", jdbc.queryForList(sql, args.toArray()));
        result.put("total", jdbc.queryForObject(cnt, Integer.class, args.toArray()));
        return result;
    }

    /* 新增/编辑任务 */
    @PostMapping("/saveNursingTask")
    public Map<String, Object> save(@RequestBody Map<String, Object> params) {
        Map<String, Object> r = new HashMap<>();
        Object idObj = params.get("id");
        if (idObj != null && Integer.parseInt(idObj.toString()) > 0) {
            jdbc.update("UPDATE t_nursing_task SET elder_id=?,item_id=?,scheduled_time=?,status=? WHERE id=?",
                params.get("elderId"), params.get("itemId"), params.get("scheduledTime"), params.get("status"), idObj);
            r.put("msg", "更新成功");
        } else {
            jdbc.update("INSERT INTO t_nursing_task (task_no,elder_id,item_id,nurse_id,scheduled_time,status,create_time) VALUES (?,?,?,?,?,?,NOW())",
                "TSK" + System.currentTimeMillis(), params.get("elderId"), params.get("itemId"), params.get("nurseId"), params.get("scheduledTime"), params.getOrDefault("status", "PENDING"));
            r.put("msg", "新增成功");
        }
        r.put("code", 200);
        return r;
    }

    /* 删除 */
    @GetMapping("/deleteNursingTask")
    public Map<String, Object> delete(Integer id) {
        jdbc.update("DELETE FROM t_nursing_task WHERE id=?", id);
        Map<String, Object> r = new HashMap<>();
        r.put("code", 200); r.put("msg", "删除成功");
        return r;
    }

    /* 执行任务 */
    @PostMapping("/executeNursingTask")
    public Map<String, Object> execute(@RequestBody Map<String, Object> params) {
        jdbc.update("UPDATE t_nursing_task SET status='DONE',execute_result=?,execute_time=NOW() WHERE id=?",
            params.getOrDefault("result", ""), params.get("id"));
        Map<String, Object> r = new HashMap<>();
        r.put("code", 200); r.put("msg", "执行完成");
        return r;
    }

    /* 负责老人列表(按护理员) */
    @PostMapping("/elderAssignmentPage")
    public Map<String, Object> assignmentPage(@RequestBody Map<String, Object> params) {
        int page = (int) params.getOrDefault("page", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        String nurseName = (String) params.getOrDefault("nurseName", "");
        int offset = (page - 1) * pageSize;

        StringBuilder where = new StringBuilder("WHERE 1=1");
        List<Object> args = new ArrayList<>();
        if (!nurseName.isEmpty()) {
            where.append(" AND u.realname LIKE ?"); args.add("%" + nurseName + "%");
        }

        String sql = "SELECT u.id as nurse_id, u.realname as nurse_name, COUNT(DISTINCT t.elder_id) as elder_count, GROUP_CONCAT(DISTINCT e.name) as elder_names FROM t_nursing_task t LEFT JOIN t_user u ON t.nurse_id=u.id LEFT JOIN t_elder e ON t.elder_id=e.id " + where + " GROUP BY u.id, u.realname ORDER BY elder_count DESC LIMIT " + offset + "," + pageSize;
        String cnt = "SELECT COUNT(DISTINCT u.id) FROM t_nursing_task t LEFT JOIN t_user u ON t.nurse_id=u.id " + where;

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", jdbc.queryForList(sql, args.toArray()));
        result.put("total", jdbc.queryForObject(cnt, Integer.class, args.toArray()));
        return result;
    }
}
