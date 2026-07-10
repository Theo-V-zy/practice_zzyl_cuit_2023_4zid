package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@TableName(value = "t_message")
@Data
public class Message implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private String messageType;
    private String receiverType;
    private Integer receiverId;
    private Integer readStatus;
    private LocalDateTime readTime;
    private Integer enabled;
    private String businessType;
    private Integer businessId;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
