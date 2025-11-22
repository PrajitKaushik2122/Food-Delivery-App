package com.prajit.foodiesDelivery.service;


import com.prajit.foodiesDelivery.entity.CartEntity;
import com.prajit.foodiesDelivery.io.CartRequest;
import com.prajit.foodiesDelivery.io.CartResponse;
import com.prajit.foodiesDelivery.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final UserService userService;

    @Override
    public CartResponse addToCart(CartRequest request) {
        String loggedInUser = userService.findByUserId();
        Optional<CartEntity> cartOptional = cartRepository.findByUserId(loggedInUser);
        CartEntity cart = cartOptional.orElseGet(()-> new CartEntity(loggedInUser, new HashMap<>()));
        Map<String,Integer> items = cart.getItems();
        items.put(request.getFoodId(), items.getOrDefault(request.getFoodId(),0)+1);
        cart.setItems(items);
        cart = cartRepository.save(cart);
        return convertToCartResponse(cart);
    }

    @Override
    public CartResponse getCart() {
        String loggedInUser = userService.findByUserId();
        CartEntity entity = cartRepository.findByUserId(loggedInUser).orElse(new CartEntity(null,loggedInUser,new HashMap<>()));
        return convertToCartResponse(entity);
    }

    private CartResponse convertToCartResponse(CartEntity cart){
        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(cart.getItems())
                .build();
    }

}
