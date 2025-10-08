package com.example.demoe.service;

import com.example.demoe.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderById(Long id);
    void deleteOrderById(Long id);
}
