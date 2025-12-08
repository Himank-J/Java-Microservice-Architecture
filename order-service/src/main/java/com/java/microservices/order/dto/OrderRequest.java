package com.java.microservices.order.dto;

import java.math.BigDecimal;

public record OrderRequest (String id, String orderNumber, String skuCode,
                           BigDecimal price, Integer quantity, UserDetails userDetails) {

    public record UserDetails(String email, String firstName, String lastName) {}
}
