package com.app.diningroom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.app.diningroom.entities.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    // method required to check if is needed to create new order for the client
    List<Orders> findByClientIdAndPaid(Long clientId, boolean isPaid);
}
