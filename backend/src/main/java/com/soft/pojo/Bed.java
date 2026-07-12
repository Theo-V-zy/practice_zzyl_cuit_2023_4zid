package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@TableName(value = "t_bed")
@Data
public class Bed implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String building;
    private String floor;
    private String roomNo;
    private String roomType;
    private String bedNo;
    private String bedCode;
    private BigDecimal bedPrice;
    private Integer elderId;
    private String deviceNo;
    private String status;
    private String remark;

    @TableField(exist = false)
    private String elderName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
