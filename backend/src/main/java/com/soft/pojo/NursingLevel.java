package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

@TableName(value ="t_nursing_level")
@Data
public class NursingLevel implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String levelName;
    private String description;
    private String islock;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
