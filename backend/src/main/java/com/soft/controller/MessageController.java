package com.soft.controller;

import com.soft.dto.MessageDto;
import com.soft.pojo.Message;
import com.soft.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping("/messagePage")
    public Map<String, Object> messagePage(@RequestBody MessageDto dto) {
        return messageService.queryMessageList(dto);
    }

    @RequestMapping("/readMessage")
    public Map<String, Object> readMessage(@RequestParam Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "操作失败");

        Message message = messageService.getById(id);
        if (message != null) {
            message.setIsRead(1);
            messageService.updateById(message);
            result.put("code", 200);
            result.put("msg", "已标记为已读");
        }

        return result;
    }

    @RequestMapping("/saveMessage")
    public Map<String, Object> saveMessage(@RequestBody Message message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "发送消息失败");

        message.setIsRead(0);
        messageService.save(message);

        result.put("code", 200);
        result.put("msg", "发送消息成功");
        return result;
    }
}
