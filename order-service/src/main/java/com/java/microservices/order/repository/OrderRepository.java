package com.java.microservices.order.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.java.microservices.order.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
    
}
