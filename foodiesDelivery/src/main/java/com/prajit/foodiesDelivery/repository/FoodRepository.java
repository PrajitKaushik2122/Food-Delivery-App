package com.prajit.foodiesDelivery.repository;

import com.prajit.foodiesDelivery.entity.FoodEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FoodRepository extends MongoRepository<FoodEntity,String> {

}
