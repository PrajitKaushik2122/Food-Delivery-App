package com.prajit.foodiesDelivery.service;

import com.prajit.foodiesDelivery.io.OrderRequest;
import com.prajit.foodiesDelivery.io.OrderResponse;
import com.razorpay.RazorpayException;

public interface OrderService {
    OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException;
}
