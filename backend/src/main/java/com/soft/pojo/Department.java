package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@TableName(value = "t_department")
@Data
public class Department implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer parentId;
    private String deptName;
    private String leader;
    private String phone;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private List<Department> children;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
