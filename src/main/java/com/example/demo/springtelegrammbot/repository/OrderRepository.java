package com.example.demo.springtelegrammbot.repository;

import com.example.demo.springtelegrammbot.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
