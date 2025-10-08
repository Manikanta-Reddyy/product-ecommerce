package com.example.demoe.repository;

import com.example.demoe.model.Order;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.product " +
            "WHERE o.id = :id")
    Optional<Order> findWithItemsAndProductsById(@Param("id") Long id);
}
