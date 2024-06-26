package com.zdzhai.example.common.service;

import com.zdzhai.example.common.model.Order;

/**
 * @author dongdong
 * @Date 2024/6/26 14:20
 */
public interface OrderService {

    /**
     * 获取订单信息
     * @param order
     * @return
     */
    Order getOrder(Order order);
}
