package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.ApplyMapper;
import com.soft.pojo.Apply;
import com.soft.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Apply> implements ApplyService {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Map<String, Object> queryApplyPage(Integer pageNum, Integer pageSize, String applyType, String status) {
        Page<Apply> page = new Page<>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10);
        QueryWrapper<Apply> params = new QueryWrapper<>();
        params.eq(StringUtils.hasText(applyType), "apply_type", applyType);
        params.eq(StringUtils.hasText(status), "status", status);
        params.orderByDesc("create_time");
        page = this.page(page, params);

        // 填充 elderName 和 applyUserName
        for (Apply a : page.getRecords()) {
            if (a.getElderId() != null) {
                try { a.setElderName(jdbc.queryForObject("SELECT name FROM t_elder WHERE id=?", String.class, a.getElderId())); } catch (Exception e) {}
            }
            if (a.getApplyUserId() != null) {
                try { a.setApplyUserName(jdbc.queryForObject("SELECT realname FROM t_user WHERE id=?", String.class, a.getApplyUserId())); } catch (Exception e) {}
            }
            if (a.getApproveUserId() != null) {
                try { a.setApproveUserName(jdbc.queryForObject("SELECT realname FROM t_user WHERE id=?", String.class, a.getApproveUserId())); } catch (Exception e) {}
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", page.getRecords());
        result.put("total", page.getTotal());
        return result;
    }
}
