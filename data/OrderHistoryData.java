package com.mawaqaa.eatandrun.data;

/**
 * Created by HP on 11/27/2017.
 */

public class OrderHistoryData {

    private String OrderId;
    private String OrderDate;
    private String OrderPrice;
    private String OrderStatus;

    public OrderHistoryData(String orderId, String orderDate, String orderPrice, String orderStatus) {
        OrderId = orderId;
        OrderDate = orderDate;
        OrderPrice = orderPrice;
        OrderStatus = orderStatus;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public void setOrderPrice(String orderPrice) {
        OrderPrice = orderPrice;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getOrderId() {
        return OrderId;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public String getOrderPrice() {
        return OrderPrice;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }
}
