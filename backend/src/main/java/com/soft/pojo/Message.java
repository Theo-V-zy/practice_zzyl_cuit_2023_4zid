package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 消息通知
 * @TableName t_message
 */
@TableName(value ="t_message")
@Data
public class Message implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;
    private String title;
    private String content;
    private String msgType;
    private Integer isRead;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
