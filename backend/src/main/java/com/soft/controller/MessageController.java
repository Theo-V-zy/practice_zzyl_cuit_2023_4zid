package com.soft.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soft.dto.MessagePageDto;
import com.soft.dto.UserLineDto;
import com.soft.pojo.Message;
import com.soft.service.MessageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping("/messages/page")
    public Map<String, Object> page(@RequestBody MessagePageDto dto, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Object online = session.getAttribute("online");
        if (online == null) {
            result.put("code", 401);
            result.put("msg", "请先登录");
            return result;
        }
        UserLineDto user = (UserLineDto) online;

        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("receiver_type", "USER");
        wrapper.eq("receiver_id", user.getId());
        wrapper.eq("enabled", 1);

        if (StringUtils.hasText(dto.getTitle())) {
            wrapper.like("title", dto.getTitle());
        }
        if (StringUtils.hasText(dto.getMessageType())) {
            wrapper.eq("message_type", dto.getMessageType());
        }
        if (dto.getReadStatus() != null) {
            wrapper.eq("read_status", dto.getReadStatus());
        }
        wrapper.orderByDesc("create_time");
        Page<Message> page = messageService.page(new Page<>(dto.getPage(), dto.getPageSize()), wrapper);
        result.put("code", 200);
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        return result;
    }

    @GetMapping("/messages/{id}")
    public Map<String, Object> detail(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        Message message = messageService.getById(id);
        if (message == null) {
            result.put("code", 400);
            result.put("msg", "消息不存在");
            return result;
        }
        // 标记为已读
        if (message.getReadStatus() == null || message.getReadStatus() == 0) {
            Message update = new Message();
            update.setId(id);
            update.setReadStatus(1);
            update.setReadTime(LocalDateTime.now());
            messageService.updateById(update);
            message.setReadStatus(1);
            message.setReadTime(update.getReadTime());
        }
        result.put("code", 200);
        result.put("data", message);
        return result;
    }

    @PostMapping("/messages/{id}/read")
    public Map<String, Object> read(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        Message update = new Message();
        update.setId(id);
        update.setReadStatus(1);
        update.setReadTime(LocalDateTime.now());
        messageService.updateById(update);
        result.put("code", 200);
        result.put("msg", "已标记为已读");
        return result;
    }

    @PostMapping("/messages/readBatch")
    public Map<String, Object> readBatch(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        @SuppressWarnings("unchecked")
        List<Integer> ids = (List<Integer>) body.get("ids");
        if (ids == null || ids.isEmpty()) {
            result.put("code", 400);
            result.put("msg", "请选择消息");
            return result;
        }
        for (Integer id : ids) {
            Message update = new Message();
            update.setId(id);
            update.setReadStatus(1);
            update.setReadTime(LocalDateTime.now());
            messageService.updateById(update);
        }
        result.put("code", 200);
        result.put("msg", "批量已读成功");
        return result;
    }

    @RequestMapping(value = "/messages/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        Message update = new Message();
        update.setId(id);
        update.setEnabled(0);
        messageService.updateById(update);
        result.put("code", 200);
        result.put("msg", "删除成功");
        return result;
    }
}
