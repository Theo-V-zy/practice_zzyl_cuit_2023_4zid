package com.soft.controller;

import com.soft.dto.OrderDto;
import com.soft.pojo.Order;
import com.soft.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orderPage")
    public Map<String, Object> orderPageList(@RequestBody OrderDto dto) {
        return orderService.queryOrderPageList(dto);
    }

    @PostMapping("/saveOrder")
    public Map<String, Object> saveOrder(@RequestBody Order order) {
        return orderService.saveOrderService(order);
    }

    @PostMapping("/updateOrder")
    public Map<String, Object> updateOrder(@RequestBody Order order) {
        orderService.updateById(order);
        Map<String, Object> r = new HashMap<>();
        r.put("code", 200); r.put("msg", "更新成功");
        return r;
    }

    @GetMapping("/deleteOrder")
    public Map<String, Object> deleteOrder(Integer id) {
        orderService.removeById(id);
        Map<String, Object> r = new HashMap<>();
        r.put("code", 200); r.put("msg", "删除成功");
        return r;
    }

    @GetMapping("/orderInfo")
    public Order orderInfo(Integer id) {
        return orderService.getById(id);
    }

    @PostMapping("/orderPay")
    public Map<String, Object> orderPay(@RequestBody Order order) {
        return orderService.payOrderService(order);
    }

    @PostMapping("/orderRefund")
    public Map<String, Object> orderRefund(@RequestBody Map<String, Object> params) {
        Order o = new Order();
        o.setId(Integer.parseInt(params.get("id").toString()));
        if (params.get("refund_amount") != null)
            o.setRefundAmount(new BigDecimal(params.get("refund_amount").toString()));
        if (params.get("refundReason") != null)
            o.setRefundReason(params.get("refundReason").toString());
        return orderService.refundOrderService(o);
    }

    @PostMapping("/financeSummary")
    public Map<String, Object> financeSummary(@RequestBody OrderDto dto) {
        return orderService.financeSummaryService(dto);
    }
}
