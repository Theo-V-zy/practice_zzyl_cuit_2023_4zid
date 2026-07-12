package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.DeviceDto;
import com.soft.mapper.DeviceMapper;
import com.soft.pojo.Device;
import com.soft.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public Map<String, Object> queryDeviceList(DeviceDto dto) {
        Page<Device> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Device> params = new QueryWrapper<>();
        params.like(StringUtils.hasText(dto.getDeviceName()), "device_name", dto.getDeviceName());
        params.eq(StringUtils.hasText(dto.getDeviceType()), "device_type", dto.getDeviceType());
        params.eq(StringUtils.hasText(dto.getOnlineStatus()), "online_status", dto.getOnlineStatus());
        params.eq(StringUtils.hasText(dto.getAlertStatus()), "alert_status", dto.getAlertStatus());
        params.orderByDesc("create_time");
        List<Device> devices = deviceMapper.selectList(page, params);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", devices);
        result.put("total", page.getTotal());
        return result;
    }
}
