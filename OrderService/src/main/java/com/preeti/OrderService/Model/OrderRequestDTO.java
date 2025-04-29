package com.preeti.OrderService.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private List<OrderItem> orderItems;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class OrderItemDTO{
    private String productId;
    private Integer quantity;
}
