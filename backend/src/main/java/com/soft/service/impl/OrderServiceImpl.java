package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.OrderDto;
import com.soft.pojo.Order;
import com.soft.service.OrderService;
import com.soft.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Map<String, Object> queryOrderPageList(OrderDto dto) {
        Page<Order> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Order> params = new QueryWrapper<>();
        params.eq(StringUtils.hasText(dto.getOrderNo()), "order_no", dto.getOrderNo());
        params.eq(StringUtils.hasText(dto.getOrderStatus()), "order_status", dto.getOrderStatus());
        params.eq(StringUtils.hasText(dto.getPayStatus()), "pay_status", dto.getPayStatus());
        params.orderByDesc("create_time");
        List<Order> orders = orderMapper.selectList(page, params);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", orders);
        result.put("total", page.getTotal());
        return result;
    }

    @Override
    public Map<String, Object> saveOrderService(Order order) {
        Map<String, Object> result = new HashMap<>();
        String orderNo = "ORD" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        order.setOrderNo(orderNo);
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        if (order.getOrderStatus() == null) order.setOrderStatus("CREATED");
        if (order.getPayStatus() == null) order.setPayStatus("UNPAID");
        orderMapper.insert(order);
        result.put("code", 200); result.put("msg", "保存成功");
        return result;
    }

    @Override
    public Map<String, Object> payOrderService(Order order) {
        Map<String, Object> result = new HashMap<>();
        Order db = orderMapper.selectById(order.getId());
        if (db == null) { result.put("code", 400); result.put("msg", "订单不存在"); return result; }
        db.setPayStatus("PAID");
        db.setOrderStatus("PAID");
        db.setUpdateTime(new Date());
        orderMapper.updateById(db);
        result.put("code", 200); result.put("msg", "收款成功");
        return result;
    }

    @Override
    public Map<String, Object> refundOrderService(Order order) {
        Map<String, Object> result = new HashMap<>();
        Order db = orderMapper.selectById(order.getId());
        if (db == null) { result.put("code", 400); result.put("msg", "订单不存在"); return result; }
        if (!"PAID".equals(db.getPayStatus())) { result.put("code", 400); result.put("msg", "仅已支付订单可退款"); return result; }
        db.setOrderStatus("REFUNDED");
        db.setRefundAmount(order.getRefundAmount());
        db.setRefundReason(order.getRefundReason());
        db.setUpdateTime(new Date());
        orderMapper.updateById(db);
        result.put("code", 200); result.put("msg", "退款成功");
        return result;
    }

    @Override
    public Map<String, Object> financeSummaryService(OrderDto dto) {
        Map<String, Object> result = new HashMap<>();
        QueryWrapper<Order> paid = new QueryWrapper<>();
        paid.eq("pay_status", "PAID");
        List<Order> paidOrders = orderMapper.selectList(paid);
        BigDecimal totalIncome = paidOrders.stream()
            .map(o -> o.getTotalAmount() != null ? o.getTotalAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        QueryWrapper<Order> refunded = new QueryWrapper<>();
        refunded.eq("order_status", "REFUNDED");
        refunded.isNotNull("refund_amount");
        List<Order> refundOrders = orderMapper.selectList(refunded);
        BigDecimal totalRefund = refundOrders.stream()
            .map(o -> o.getRefundAmount() != null ? o.getRefundAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        result.put("code", 200);
        result.put("totalIncome", totalIncome);
        result.put("totalRefund", totalRefund);
        result.put("pendingCount", orderMapper.selectCount(new QueryWrapper<Order>().eq("pay_status", "UNPAID")));
        result.put("totalCount", orderMapper.selectCount(null));
        return result;
    }
}
