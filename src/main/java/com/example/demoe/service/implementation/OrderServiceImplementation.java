package com.example.demoe.service.implementation;

import com.example.demoe.cache.OrderCacheService;
import com.example.demoe.dto.OrderDto;
import com.example.demoe.dto.OrderItemDto;
import com.example.demoe.exception.ResourceNotFoundException;
import com.example.demoe.mapper.OrderMapper;
import com.example.demoe.model.Order;
import com.example.demoe.model.OrderItem;
import com.example.demoe.model.Product;
import com.example.demoe.model.User;
import com.example.demoe.repository.OrderRepository;
import com.example.demoe.repository.ProductRepository;
import com.example.demoe.repository.UserRepository;
import com.example.demoe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImplementation implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderCacheService orderCacheService;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {

        Order order = orderMapper.toEntity(orderDto);

        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        order.setUser(user);

        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItemDto> orderItemDtos = orderDto.getItems();

        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem orderItem = orderItems.get(i);
            OrderItemDto orderItemDto = orderItemDtos.get(i);

            Product product = productRepository.findById(orderItemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setUnitPrice(product.getPrice());
        }

        BigDecimal totalAmount = order.getOrderItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        Order fullOrder = orderRepository.findWithItemsAndProductsById(savedOrder.getId())
                .orElseThrow(() -> new RuntimeException("Order not found after save"));

        OrderDto dto = orderMapper.toDto(fullOrder);

        orderCacheService.put(dto);

        return dto;
    }

    @Override
    public OrderDto getOrderById(Long id) {

        OrderDto cached = orderCacheService.get(id);

        if (cached != null) {
            return cached;
        }

        Order saved = orderRepository.findWithItemsAndProductsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found"));

        OrderDto dto = orderMapper.toDto(saved);

        orderCacheService.put(dto);

        return dto;
    }

    @Override
    public void deleteOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        orderCacheService.delete(id);

        order.setStatus("DELETED");

        orderRepository.save(order);
    }
}
