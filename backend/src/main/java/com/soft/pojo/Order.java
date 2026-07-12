package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@TableName(value ="t_order")
@Data
public class Order implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String orderNo;
    private Integer familyId;
    private Integer elderId;
    private Integer serviceItemId;
    private String serviceName;
    private Integer quantity;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private String payStatus;
    private String orderStatus;
    private BigDecimal refundAmount;
    private String refundReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
