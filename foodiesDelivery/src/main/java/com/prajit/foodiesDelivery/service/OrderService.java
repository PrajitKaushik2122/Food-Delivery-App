package com.prajit.foodiesDelivery.service;

import com.prajit.foodiesDelivery.io.OrderRequest;
import com.prajit.foodiesDelivery.io.OrderResponse;
import com.razorpay.RazorpayException;

import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException;
    void verifyPayment(Map<String,String> paymentData,String status);
    List<OrderResponse> getUserOrders();
    void removeOrder(String orderId);
    List<OrderResponse> getAllOrders();
    void updateOrderStatus(String orderId, String status);
}
