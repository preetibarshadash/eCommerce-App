package com.preeti.OrderService.Service;

import com.preeti.OrderService.Model.Order;
import com.preeti.OrderService.Repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;  // Jackson ObjectMapper to convert JSON to objects

    public KafkaConsumer(OrderRepository orderRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-created", groupId = "order_group")
    public void consumeOrder(String orderString) {
        try {
            // Deserialize the orderString JSON into an Order object
            Order order = objectMapper.readValue(orderString, Order.class);

            // Set order status as "CONFIRMED" after consumption
            order.setStatus("CONFIRMED");

            // Save the order to the database
            orderRepository.save(order);

            System.out.println("Order Processed: " + order.getId());
        } catch (Exception e) {
            System.err.println("Error processing order: " + orderString);
            e.printStackTrace();
        }
    }
}
