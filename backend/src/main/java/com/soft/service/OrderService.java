package com.soft.service;

import com.soft.dto.OrderDto;
import com.soft.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author MemberB
* @description 针对表【t_order】的数据库操作Service
* @createDate 2026-07-10
*/
public interface OrderService extends IService<Order> {

    /*分页查询订单列表*/
    Map<String, Object> queryOrderPageList(OrderDto orderDto);

    /*保存订单*/
    Map<String, Object> saveOrderService(Order order);

    /*确认收款*/
    Map<String, Object> payOrderService(Order order);

    /*处理退款*/
    Map<String, Object> refundOrderService(Order order);

    /*财务统计*/
    Map<String, Object> financeSummaryService(OrderDto orderDto);
}
