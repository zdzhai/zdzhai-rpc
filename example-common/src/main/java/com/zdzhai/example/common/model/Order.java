package com.zdzhai.example.common.model;

import java.io.Serializable;

/**
 * @author dongdong
 * @Date 2024/6/26 14:18
 * 订单类
 */
public class Order implements Serializable {

    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
