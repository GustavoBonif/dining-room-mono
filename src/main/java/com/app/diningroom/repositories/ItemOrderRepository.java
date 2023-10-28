package com.app.diningroom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.diningroom.entities.ItemOrder;

public interface ItemOrderRepository extends JpaRepository<ItemOrder, Long> {
}
