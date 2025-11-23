package com.prajit.foodiesDelivery.service;


import com.prajit.foodiesDelivery.entity.OrderEntity;
import com.prajit.foodiesDelivery.io.OrderRequest;
import com.prajit.foodiesDelivery.io.OrderResponse;
import com.prajit.foodiesDelivery.repository.OrderRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;

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
                .build();

    }
}
