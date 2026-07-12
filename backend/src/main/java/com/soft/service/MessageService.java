package com.soft.service;

import com.soft.dto.MessageDto;
import com.soft.pojo.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;

public interface MessageService extends IService<Message> {
    Map<String, Object> queryMessageList(MessageDto dto);
}
