package com.java.microservices.order.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.java.microservices.order.client.InventoryClient;
import com.java.microservices.order.dto.OrderRequest;
import com.java.microservices.order.event.OrderPlacedEvent;
import com.java.microservices.order.model.Order;
import com.java.microservices.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest) {
        
        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if (isProductInStock) {
            // map OrderRequest to Order entity
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price().multiply(BigDecimal.valueOf(orderRequest.quantity())));
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());
            
            // save order to order repository
            orderRepository.save(order);

            // Send the message to Kafka Topic
            // OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
            // orderPlacedEvent.setOrderNumber(order.getOrderNumber());
            // orderPlacedEvent.setEmail(orderRequest.userDetails().email());
            // orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
            // orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());
            // log.info("Start - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
            // kafkaTemplate.send("order-placed", orderPlacedEvent);
            // log.info("End - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);

        } else {
            throw new RuntimeException("Product with SkuCode " + orderRequest.skuCode() + " is not in stock");
        }
    }
}
