package com.preeti.OrderService.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "order_items")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItem {

    @Id
    private String id;
    private String productId;
    private Integer quantity;

    public OrderItem(String productId, Integer quantity) {
    }
}
