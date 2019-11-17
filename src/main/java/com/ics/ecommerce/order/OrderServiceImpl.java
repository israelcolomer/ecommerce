package com.ics.ecommerce.order;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    // TODO: once authentication is implemented, use the authentication data to populate the customer information on the order
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findOrdersWithinDateRange(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findOrdersWithinDateRange(start, end);
    }

}
