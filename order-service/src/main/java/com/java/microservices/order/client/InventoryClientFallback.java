package com.java.microservices.order.client;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InventoryClientFallback {

    public boolean fallbackMethod(String skuCode, Integer quantity, Throwable throwable) {
        log.error("Inventory service failed: {}", throwable.getMessage());
        return false;
    }
}
