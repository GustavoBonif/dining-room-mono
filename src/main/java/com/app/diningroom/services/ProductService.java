package com.app.diningroom.services;

import com.app.diningroom.dto.ProductDTO;
import com.app.diningroom.entities.Product;
import com.app.diningroom.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional
    public ProductDTO findById(Long id) {

        Product entity = repository.findById(id).get();
        ProductDTO dto = new ProductDTO(entity);
        return dto;
    }
}
