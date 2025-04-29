package com.preeti.OrderService.Controller;

import com.preeti.OrderService.Model.Order;
import com.preeti.OrderService.Model.OrderRequestDTO;
import com.preeti.OrderService.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OrderResolver {

    public OrderResolver(OrderService orderService) {
        this.orderService = orderService;
    }

    private final OrderService orderService;

    @MutationMapping
    public Order createOrder(
            @Argument OrderRequestDTO orderRequest,
            @ContextValue(name = "Authorization") String token) {
        return orderService.createOrder(orderRequest, token);
    }

    @QueryMapping
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }
}
