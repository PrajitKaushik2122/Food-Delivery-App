package com.prajit.foodiesDelivery.service;


import com.prajit.foodiesDelivery.entity.OrderEntity;
import com.prajit.foodiesDelivery.io.OrderRequest;
import com.prajit.foodiesDelivery.io.OrderResponse;
import com.prajit.foodiesDelivery.repository.CartRepository;
import com.prajit.foodiesDelivery.repository.OrderRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private CartRepository cartRepository;

    @Value("${razorpay.key}")
    private String RAZORPAY_KEY;

    @Value("${razorpay.secret}")
    private String RAZORPAY_SECRET;
    @Override
    public OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException {
        OrderEntity newOrder = convertToOrderEntity(request);
        newOrder = orderRepository.save(newOrder);

        RazorpayClient razorpayClient = new RazorpayClient(RAZORPAY_KEY,RAZORPAY_SECRET);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount",newOrder.getAmount());
        orderRequest.put("currency","INR");
        orderRequest.put("payment_capture",1);

        Order razorpayOrder = razorpayClient.orders.create(orderRequest);
        newOrder.setRazorpayOrderId(razorpayOrder.get("id"));
        String loggedInUser = userService.findByUserId();
        newOrder.setUserId(loggedInUser);
        newOrder = orderRepository.save(newOrder);
        return convertToOrderResponse(newOrder);
    }

    @Override
    public void verifyPayment(Map<String, String> paymentData, String status) {
        String razorpayOrderId = paymentData.get("razorpay_order_id");
        OrderEntity existingOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId).orElseThrow(()->new RuntimeException("order not found"));
        existingOrder.setPaymentStatus(status);
        existingOrder.setRazorpaySignature(paymentData.get("razorpay_signature"));
        existingOrder.setRazorpayPaymentId(paymentData.get("razorpay_payment_id"));
        orderRepository.save(existingOrder);
        if("paid".equalsIgnoreCase(status)){
            cartRepository.deleteByUserId(existingOrder.getUserId());
        }
    }

    @Override
    public List<OrderResponse> getUserOrders() {
        String loggedInUser = userService.findByUserId();
        List<OrderEntity> list = orderRepository.findByUserId(loggedInUser);
        return list.stream().map(entity->convertToOrderResponse(entity)).collect(Collectors.toList());
    }

    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<OrderEntity> orders =  orderRepository.findAll();
        return orders.stream().map(entity->convertToOrderResponse(entity)).collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(()-> new RuntimeException("Order not found"));
        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    private OrderEntity convertToOrderEntity(OrderRequest request){
        return OrderEntity.builder()
                .userAddress(request.getUserAddress())
                .amount(request.getAmount())
                .orderedItems(request.getOrderedItems())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .orderStatus(request.getOrderStatus())
                .build();
    }

    private OrderResponse convertToOrderResponse(OrderEntity request){
        return OrderResponse.builder()
                .id(request.getId())
                .amount(request.getAmount())
                .userAddress(request.getUserAddress())
                .userId(request.getUserId())
                .razorpayId(request.getRazorpayOrderId())
                .paymentStatus(request.getPaymentStatus())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .orderStatus(request.getOrderStatus())
                .orderItems(request.getOrderedItems())
                .build();

    }
}
