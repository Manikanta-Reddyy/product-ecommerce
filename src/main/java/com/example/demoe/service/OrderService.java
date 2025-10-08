package com.example.demoe.service;

import com.example.demoe.dto.OrderDto;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderById(Long id);
    void deleteOrderById(Long id);
}
