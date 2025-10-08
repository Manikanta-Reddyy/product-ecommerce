package com.example.demoe.mapper;

import com.example.demoe.dto.OrderItemDto;
import com.example.demoe.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "productId", source = "product.id")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem toEntity(OrderItemDto orderItemDto);
}
