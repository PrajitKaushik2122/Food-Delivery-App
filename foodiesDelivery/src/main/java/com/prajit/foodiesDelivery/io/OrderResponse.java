package com.prajit.foodiesDelivery.io;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderResponse {
    private String id;
    private String userId;
    private String phoneNumber;
    private String email;
    private String userAddress;
    private double amount;
    private String paymentStatus;
    private String razorpayId;
    private String orderStatus;
}
