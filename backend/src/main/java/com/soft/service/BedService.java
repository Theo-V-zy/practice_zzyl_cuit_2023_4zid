package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.pojo.Bed;

import java.util.Map;

public interface BedService extends IService<Bed> {
    Map<String, Object> queryBedPage(Integer pageNum, Integer pageSize, String roomNo, String status);
}
