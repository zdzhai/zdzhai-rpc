package com.zdzhai.example.provider;

import com.zdzhai.example.common.model.Order;
import com.zdzhai.example.common.model.User;
import com.zdzhai.example.common.service.OrderService;
import com.zdzhai.example.common.service.UserService;

/**
 * @author dongdong
 * @Date 2024/6/20 20:58
 * 实现用户服务类，打印用户名称并返回用户对象
 */
public class OrderServiceImpl implements OrderService {

    @Override
    public Order getOrder(Order order) {
        order.setOrderId(1000L);
        return order;
    }
}
