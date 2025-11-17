package com.prajit.foodiesDelivery.controller;


import com.prajit.foodiesDelivery.io.UserRequest;
import com.prajit.foodiesDelivery.io.UserResponse;
import com.prajit.foodiesDelivery.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody UserRequest body){
        return userService.registerUser(body);
    }
}
