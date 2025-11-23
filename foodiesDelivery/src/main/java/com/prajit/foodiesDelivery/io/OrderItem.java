package com.prajit.foodiesDelivery.io;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItem {
    private String foodId;
    private int qty;
    private double price;
    private String category;
    private String name;
    private String desc;
    private String imgUrl;
}
