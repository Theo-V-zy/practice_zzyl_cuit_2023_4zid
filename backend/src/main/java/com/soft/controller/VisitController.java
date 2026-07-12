package com.soft.controller;

import com.soft.dto.VisitDto;
import com.soft.pojo.Visit;
import com.soft.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class VisitController {

    @Autowired
    private VisitService visitService;

    private static final DateTimeFormatter NO_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private String genVisitNo() {
        return "VST" + LocalDateTime.now().format(NO_FMT) + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    @RequestMapping("/visits/page")
    public Map<String, Object> page(@RequestBody VisitDto dto) {
        return visitService.queryVisitList(dto);
    }

    @PostMapping("/visits")
    public Map<String, Object> add(@RequestBody Visit visit) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (visit.getVisitorName() == null || visit.getVisitorName().trim().isEmpty()) {
            result.put("msg", "访客姓名不能为空");
            return result;
        }
        if (visit.getElderId() == null) {
            result.put("msg", "请选择老人");
            return result;
        }

        visit.setVisitNo(genVisitNo());
        if (visit.getVisitStage() == null || visit.getVisitStage().isEmpty()) {
            visit.setVisitStage("RESERVATION");
        }
        if (visit.getStatus() == null || visit.getStatus().isEmpty()) {
            visit.setStatus("PENDING");
        }
        visit.setCreateTime(LocalDateTime.now());

        boolean ok = visitService.save(visit);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "新增成功" : "新增失败");
        return result;
    }

    @RequestMapping(value = "/visits", method = RequestMethod.PUT)
    public Map<String, Object> update(@RequestBody Visit visit) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        if (visit.getId() == null) {
            result.put("msg", "ID不能为空");
            return result;
        }

        visit.setUpdateTime(LocalDateTime.now());
        boolean ok = visitService.updateById(visit);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "修改成功" : "修改失败");
        return result;
    }

    @RequestMapping(value = "/visits/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        boolean ok = visitService.removeById(id);
        result.put("code", ok ? 200 : 400);
        result.put("msg", ok ? "删除成功" : "删除失败");
        return result;
    }

    @RequestMapping("/visits/confirmArrive")
    public Map<String, Object> confirmArrive(@RequestBody Visit visit) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "到院确认失败");

        Visit db = visitService.getById(visit.getId());
        if (db != null) {
            db.setVisitStage("ARRIVAL");
            db.setStatus("CONFIRMED");
            db.setArriveTime(LocalDateTime.now());
            db.setUpdateTime(LocalDateTime.now());
            visitService.updateById(db);

            result.put("code", 200);
            result.put("msg", "到院确认成功");
        }
        return result;
    }
}
