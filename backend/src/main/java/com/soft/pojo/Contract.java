package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@TableName(value = "t_contract")
@Data
public class Contract implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String contractNo;
    private Integer elderId;
    private Integer familyId;
    private Integer bedId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal deposit;
    private BigDecimal monthlyFee;
    private String status;
    private String fileUrl;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String elderName;
    @TableField(exist = false)
    private String bedNo;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
