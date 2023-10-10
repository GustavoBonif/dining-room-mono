package com.app.diningroom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.diningroom.entities.Product;
public interface ProductRepository extends JpaRepository<Product, Long> {
}
