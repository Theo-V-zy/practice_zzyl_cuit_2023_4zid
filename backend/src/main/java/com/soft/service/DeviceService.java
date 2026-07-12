package com.soft.service;

import com.soft.dto.DeviceDto;
import com.soft.pojo.Device;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;

public interface DeviceService extends IService<Device> {
    Map<String, Object> queryDeviceList(DeviceDto dto);
    String getMaxDeviceNo();
}
