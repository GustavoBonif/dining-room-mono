package com.app.diningroom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.diningroom.entities.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByProductId(Long product_id);
}
