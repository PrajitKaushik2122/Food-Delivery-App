package com.prajit.foodiesDelivery.service;

import com.prajit.foodiesDelivery.io.FoodRequest;
import com.prajit.foodiesDelivery.io.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FoodService {

    String uploadFile(MultipartFile file);
    FoodResponse addFood(FoodRequest request, MultipartFile file);
}
