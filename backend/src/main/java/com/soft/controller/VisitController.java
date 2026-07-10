package com.soft.controller;

import com.soft.dto.VisitDto;
import com.soft.pojo.Visit;
import com.soft.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/visitPage")
    public Map<String, Object> visitPage(@RequestBody VisitDto dto) {
        return visitService.queryVisitList(dto);
    }

    @RequestMapping("/saveVisit")
    public Map<String, Object> saveVisit(@RequestBody Visit visit) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "添加失败");

        visit.setVisitNo(genVisitNo());
        if (visit.getVisitStage() == null || visit.getVisitStage().isEmpty()) {
            visit.setVisitStage("RESERVATION");
        }
        if (visit.getStatus() == null || visit.getStatus().isEmpty()) {
            visit.setStatus("PENDING");
        }
        visit.setCreateTime(LocalDateTime.now());
        visitService.save(visit);

        result.put("code", 200);
        result.put("msg", "添加成功");
        return result;
    }

    @RequestMapping("/updateVisit")
    public Map<String, Object> updateVisit(@RequestBody Visit visit) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "更新失败");

        visit.setUpdateTime(LocalDateTime.now());
        visitService.updateById(visit);

        result.put("code", 200);
        result.put("msg", "更新成功");
        return result;
    }

    @RequestMapping("/deleteVisit")
    public Map<String, Object> deleteVisit(Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "删除失败");

        visitService.removeById(id);

        result.put("code", 200);
        result.put("msg", "删除成功");
        return result;
    }

    @RequestMapping("/confirmArrive")
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
