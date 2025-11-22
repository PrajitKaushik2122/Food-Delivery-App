package com.prajit.foodiesDelivery.controller;


import com.prajit.foodiesDelivery.io.CartRequest;
import com.prajit.foodiesDelivery.io.CartResponse;
import com.prajit.foodiesDelivery.service.CartService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public CartResponse addToCart(@RequestBody CartRequest req){
        String foodId = req.getFoodId();
        if(foodId==null || foodId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"foodId required");
        }
        return cartService.addToCart(req);
    }

    @GetMapping("/get")
    public CartResponse getCart(){
        return cartService.getCart();
    }

    @DeleteMapping("/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(){
        cartService.clearCart();
    }

    @DeleteMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public CartResponse removeFromCart(@RequestBody CartRequest request){
        String foodId = request.getFoodId();
        if(foodId==null || foodId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"foodId required");
        }
        return cartService.removeFromCart(request);
    }
}
