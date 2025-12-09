# Shopping Management System

This project is a comprehensive demonstration of Microservices architecture using Spring Boot. It explores key concepts such as synchronous/asynchronous communication, service discovery, distributed tracing, and API Gateways.

## Project Overview

The primary goal of this repository is to understand how different microservices interact and work together to form a complete shopping management system.

### Core Concepts Covered
1.  **Microservice Communication**:
    -   **Synchronous**: Using `RestClient` for direct service-to-service calls (e.g., Order Service calling Inventory Service).
    -   **Asynchronous**: Using **Kafka** for event-driven communication (e.g., Order Service sending notifications).
2.  **REST API Development**: Using `RestController` to define endpoints.
3.  **Data Persistence**: Leveraging `MongoRepository` for MongoDB operations (Product Service, Order Service).
4.  **Service Discovery**: Using Netflix **Eureka** for dynamic service registration and discovery.
5.  **API Gateway**: Implementing centralized entry points using both **Spring Cloud Gateway MVC** and **Spring Cloud Gateway (Reactive)**.
6.  **Resilience**: Circuit breakers and fallback mechanisms.

---

## Service Architecture & Flow

<img width="1026" height="514" alt="image" src="https://github.com/user-attachments/assets/0dba2f47-55c5-438c-a368-d8338a9c06b9" />

The system consists of the following core services:

1.  **Product Service** (`product-service`):
    -   Manages product catalog.
    -   Allows creating new products and listing all available products.
    -   Database: MongoDB.

2.  **Order Service** (`order-service`):
    -   Handles order placement.
    -   **Flow**:
        1.  Receives an order request containing product `skuCode` and `quantity`.
        2.  **Validation**: A synchronous call (via `RestClient`) is made to the **Inventory Service** to check stock availability.
        3.  **In Stock**: If the inventory is sufficient, the order is placed/persisted in the database.
        4.  **Notification**: A message is published to a **Kafka** topic (`order-placed`) to notify the user asynchronously.
    -   **Out of Stock**: If the inventory check fails, the order is rejected.

3.  **Inventory Service** (`inventory-service`):
    -   Manages stock levels for products.
    -   Validates whether a specific `skuCode` has sufficient quantity for an order.

4.  **Notification Service** (`notification-service`):
    -   Listens to the Kafka `order-placed` topic.
    -   On receiving an event, it simulates sending an email confirmation to the user.

---

## API Gateways

This project demonstrates two different approaches to implementing an API Gateway:

### 1. API Gateway MVC (`api-gateway`)
-   **Tech Stack**: Spring Cloud Gateway MVC (based on Spring MVC/Tomcat).
-   **Model**: Blocking (Imperative).
-   **Usage**: Defines custom routes, filters, and fallbacks explicitly in code or configuration.
-   **Key Feature**: Useful when you prefer a traditional servlet-based stack or need to integrate with other blocking libraries.

### 2. API Gateway Eureka (`api-gateway-eureka`)
-   **Tech Stack**: Spring Cloud Gateway (Reactive/Netty) + Eureka Client.
-   **Model**: Non-blocking (Reactive) & Dynamic.
-   **Usage**: Automatically discovers services registered with the **Eureka Server** (`discovery-service`).
-   **Key Feature**: Dynamic routing. It routes traffic to services based on their service aliases (e.g., `LB://PRODUCT-SERVICE`) without hardcoding URLs. It handles load balancing automatically.

---

## Technical Highlights

### Inter-Service Communication
-   **RestClient**: Used in `Order Service` to query `Inventory Service`. This simulates a tight coupling where an immediate response is required (is the item in stock?).
    ```java
    // Example concept
    restClient.get()
        .uri("http://inventory-service/api/inventory?skuCode={code}&quantity={qty}")
        .retrieve()
        .toBodilessEntity();
    ```

### Event-Driven Architecture (Kafka)
-   Decouples the Order processing from the Notification system.
-   The Order Service acts as the **Producer**.
-   The Notification Service acts as the **Consumer**.
-   Ensures that if the Notification service is down, order placement is not blocked.

### Discovery Server
-   A standalone **Eureka Server** (`discovery-service`) acts as the phonebook.
-   All microservices (Product, Order, Inventory, Gateway Eureka) register themselves here.

### Fault Tolerance
-   **Circuit Breaker**: Prevents cascading failures when a downstream service is unavailable.
-   **Retry**: Automatically retries failed operations to handle transient errors.
-   **Rate Limiting**: Restricts the rate of incoming requests to protect resources.

---

## Get Started

### Option 1: Running with MVC Gateway (Standalone)
1.  Run `api-gateway`
2.  Run `product-service`
3.  Run `order-service`
4.  Run `notification-service`
5.  Run `inventory-service`

### Option 2: Running with Eureka (Service Discovery)
1.  Run `discovery-service`
2.  Run `api-gateway-eureka`
3.  Run `product-service`
4.  Run `order-service`
5.  Run `notification-service`
6.  Run `inventory-service`
