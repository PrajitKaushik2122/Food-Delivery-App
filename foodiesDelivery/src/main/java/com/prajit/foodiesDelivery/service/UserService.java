package com.prajit.foodiesDelivery.service;

import com.prajit.foodiesDelivery.io.UserRequest;
import com.prajit.foodiesDelivery.io.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRequest request);
}
