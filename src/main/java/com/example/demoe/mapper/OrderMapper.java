package com.example.demoe.mapper;

import com.example.demoe.dto.OrderDto;
import com.example.demoe.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "items", source = "orderItems")
    OrderDto toDto(Order order);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orderItems", source = "items")
    Order toEntity(OrderDto orderDto);
}
