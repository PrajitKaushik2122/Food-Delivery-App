package com.prajit.foodiesDelivery.repository;

import com.prajit.foodiesDelivery.entity.CartEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<CartEntity,String> {
    Optional<CartEntity> findByUserId(String id);
    void deleteByUserId(String id);
}
