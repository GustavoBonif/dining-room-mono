package com.app.diningroom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.app.diningroom.entities.Brand;
import com.app.diningroom.entities.Product;
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByBrand(Brand brand);
}
