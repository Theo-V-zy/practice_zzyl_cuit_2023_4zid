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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceServiceImpl extends
        ServiceImpl<DeviceMapper, Device>
    implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public Map<String, Object> queryDeviceList(DeviceDto dto) {
        Page<Device> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Device> params = new QueryWrapper<>();

        String deviceName = dto.getDeviceName();
        String deviceType = dto.getDeviceType();
        String runningStatus = dto.getRunningStatus();
        String alarmStatus = dto.getAlarmStatus();
        String islock = dto.getIslock();

        params.like(!StringUtils.isEmpty(deviceName), "device_name", deviceName);
        params.eq(!StringUtils.isEmpty(deviceType), "device_type", deviceType);
        params.eq(!StringUtils.isEmpty(runningStatus), "running_status", runningStatus);
        params.eq(!StringUtils.isEmpty(alarmStatus), "alarm_status", alarmStatus);
        params.eq(!StringUtils.isEmpty(islock), "islock", islock);
        params.orderByDesc("create_time");

        List<Device> devices = deviceMapper.selectList(page, params);

        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("devices", devices);
        return result;
    }
}
