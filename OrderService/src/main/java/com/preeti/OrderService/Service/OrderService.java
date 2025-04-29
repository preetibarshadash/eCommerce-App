package com.preeti.OrderService.Service;

import com.preeti.OrderService.Model.Order;
import com.preeti.OrderService.Model.OrderItem;
import com.preeti.OrderService.Model.OrderRequestDTO;
import com.preeti.OrderService.Repository.OrderRepository;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import com.preeti.OrderService.config.JwtUtil;

@Getter
@Data
@Service
public class OrderService {

//    private final JwtUtil jwtUtil;
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, Order> kafkaTemplate; // Inject KafkaTemplate

    public OrderService(OrderRepository orderRepository, WebClient.Builder webClientBuilder, KafkaTemplate<String, Order> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
        this.kafkaTemplate = kafkaTemplate;
    }

    private static final String ORDER_TOPIC = "order_topic"; // Topic name

    public Order createOrder(OrderRequestDTO request, String token) {

//        String userId = jwtUtil.extractUsername(token);

        // Fetch product details
        List<Map<String, Object>> products = fetchAllProducts(token);

        Map<String, Double> productPriceMap = products.stream()
                .collect(Collectors.toMap(
                        p -> (String) p.get("id"),
                        p -> ((Number) p.get("price")).doubleValue()
                ));

        // Calculate total amount
        double totalAmount = 0.0;
        List<OrderItem> orderProducts = new ArrayList<>();

        for (var item : request.getOrderItems()) {
            Double price = productPriceMap.get(item.getProductId());
            if (price == null) {
                throw new RuntimeException("Product not found: " + item.getProductId());
            }
            totalAmount += price * item.getQuantity();
            System.out.println(item);
            orderProducts.add(item);
        }

        Order order = new Order();
        order.setUserId("userId");
        order.setOrderItems(orderProducts);
        order.setTotalAmount(totalAmount);
        order.setStatus("Created");

        Order savedOrder = orderRepository.save(order);

        // Send the order to Kafka
        kafkaTemplate.send(ORDER_TOPIC, savedOrder);

        return savedOrder;
    }

    private List<Map<String, Object>> fetchAllProducts(String token) {
        String graphqlQuery = "{ getAllProducts { id name price } }";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", "query " + graphqlQuery);
        requestBody.put("variables", new HashMap<>());

        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8765/product-service/graphql")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    Map<String, Object> data = (Map<String, Object>) response.get("data");
                    return (List<Map<String, Object>>) data.get("getAllProducts");
                })
                .block();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
