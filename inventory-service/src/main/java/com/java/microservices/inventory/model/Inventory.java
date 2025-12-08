package com.java.microservices.inventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(value = "inventory")
public class Inventory {
    
    @Id
    private String id;
    private String skuCode;
    private Integer quantity;
}
