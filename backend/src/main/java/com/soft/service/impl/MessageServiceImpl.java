package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.MessageDto;
import com.soft.mapper.MessageMapper;
import com.soft.pojo.Message;
import com.soft.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl extends
        ServiceImpl<MessageMapper, Message>
    implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public Map<String, Object> queryMessageList(MessageDto dto) {
        Page<Message> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Message> params = new QueryWrapper<>();

        Integer userId = dto.getUserId();
        String msgType = dto.getMsgType();
        Integer isRead = dto.getIsRead();

        params.eq(userId != null, "user_id", userId);
        params.eq(!StringUtils.isEmpty(msgType), "msg_type", msgType);
        params.eq(isRead != null, "is_read", isRead);
        params.orderByDesc("create_time");

        List<Message> messages = messageMapper.selectList(page, params);

        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("messages", messages);
        return result;
    }
}
