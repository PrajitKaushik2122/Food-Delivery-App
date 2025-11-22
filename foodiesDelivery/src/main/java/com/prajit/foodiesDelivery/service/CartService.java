package com.prajit.foodiesDelivery.service;

import com.prajit.foodiesDelivery.io.CartRequest;
import com.prajit.foodiesDelivery.io.CartResponse;

public interface CartService {
    CartResponse addToCart(CartRequest request);
    CartResponse getCart();
    void clearCart();
    CartResponse removeFromCart(CartRequest request);
}
