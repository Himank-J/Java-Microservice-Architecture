package com.java.microservices.inventory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.java.microservices.inventory.model.Inventory;

public interface InventoryRepository extends MongoRepository<Inventory, String> {
    
    boolean existsBySkuCodeAndQuantityIsGreaterThanEqual(String skuCode, Integer quantity);
}

