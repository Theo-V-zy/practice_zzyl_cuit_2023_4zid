package com.soft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {

    private static JdbcTemplate jdbc;

    @Autowired
    public void setJdbc(JdbcTemplate jdbcTemplate) {
        MessageUtil.jdbc = jdbcTemplate;
    }

    /** 发送消息：title标题, content内容, type类型(系统通知/业务提醒/报警通知), businessType业务来源 */
    public static void send(String title, String content, String type, String businessType) {
        try {
            jdbc.update("INSERT INTO t_message(title,content,message_type,receiver_type,receiver_id,read_status,enabled,business_type,create_time) VALUES(?,?,?,'USER',1,0,1,?,NOW())",
                title, content, type, businessType);
        } catch (Exception e) {
            System.err.println("消息写入失败: " + e.getMessage());
        }
    }
}
