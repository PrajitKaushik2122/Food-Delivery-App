package com.prajit.foodiesDelivery.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prajit.foodiesDelivery.io.FoodRequest;
import com.prajit.foodiesDelivery.io.FoodResponse;
import com.prajit.foodiesDelivery.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@AllArgsConstructor
public class FoodController {

    private FoodService foodService;

    @PostMapping("/add")
    public FoodResponse addFood(@RequestPart("dish") String foodString,
                                @RequestPart("file") MultipartFile file){
        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest foodRequest = null;
        try {
            foodRequest = objectMapper.readValue(foodString,FoodRequest.class);
        } catch (JsonProcessingException e) {
            System.out.println(e);
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"object mapping failed");
        }
        FoodResponse foodResponse = foodService.addFood(foodRequest,file);
        return foodResponse;
    }

    @GetMapping("/getAll")
    public List<FoodResponse> getFoods(){
        return foodService.readFoods();
    }


}
