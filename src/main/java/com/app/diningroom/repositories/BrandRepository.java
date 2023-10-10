package com.app.diningroom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.diningroom.entities.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
