package com.soft.controller;

import com.soft.dto.DeviceDto;
import com.soft.pojo.Device;
import com.soft.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @RequestMapping("/devicePage")
    public Map<String, Object> devicePage(@RequestBody DeviceDto dto) {
        return deviceService.queryDeviceList(dto);
    }

    @RequestMapping("/saveDevice")
    public Map<String, Object> saveDevice(@RequestBody Device device) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "添加设备失败");

        // 自动生成设备编号
        if (device.getDeviceNo() == null || device.getDeviceNo().isEmpty()) {
            String maxNo = deviceService.getMaxDeviceNo();
            String nextNo;
            if (maxNo == null) {
                nextNo = "DEV001";
            } else {
                int num = Integer.parseInt(maxNo.substring(3)) + 1;
                nextNo = "DEV" + String.format("%03d", num);
            }
            device.setDeviceNo(nextNo);
        }

        device.setCreateTime(new Date());
        deviceService.save(device);

        result.put("code", 200);
        result.put("msg", "添加设备成功");
        return result;
    }

    @RequestMapping("/updateDevice")
    public Map<String, Object> updateDevice(@RequestBody Device device) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "更新设备失败");

        deviceService.updateById(device);

        result.put("code", 200);
        result.put("msg", "更新设备成功");
        return result;
    }

    @RequestMapping("/deleteDevice")
    public Map<String, Object> deleteDevice(Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "删除设备失败");

        deviceService.removeById(id);

        result.put("code", 200);
        result.put("msg", "删除设备成功");
        return result;
    }

    @RequestMapping("/updateDeviceLock")
    public Map<String, Object> updateDeviceLock(@RequestBody Device device) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "操作失败");

        Device exist = deviceService.getById(device.getId());
        if (exist != null) {
            exist.setStatus(device.getStatus());
            deviceService.updateById(exist);
            result.put("code", 200);
            result.put("msg", "操作成功");
        }

        return result;
    }

    @RequestMapping("/getDeviceById")
    public Map<String, Object> getDeviceById(Integer id) {
        Map<String, Object> result = new HashMap<>();
        Device device = deviceService.getById(id);
        result.put("code", 200);
        result.put("data", device);
        return result;
    }
}
