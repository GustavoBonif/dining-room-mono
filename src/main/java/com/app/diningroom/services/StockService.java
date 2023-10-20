package com.app.diningroom.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.app.diningroom.entities.Stock;
import com.app.diningroom.repositories.StockRepository;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;
//    Autowired
//    private ProductService productService;

    @Transactional
    public ResponseEntity<Stock> findById(Long id) {
        Optional<Stock> optionalStock = this.stockRepository.findById(id);
        return optionalStock.map(stock -> new ResponseEntity<>(stock, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public List<Stock> listAll() {
        return this.stockRepository.findAll();
    }
//
//    @Transactional
//    public ResponseEntity<String> createStock(StockDTO stockDTO) {
//        if(!this.productService.existsById(stockDTO.getProduct_id())) {
//            return new ResponseEntity<>("Produto com ID " + stockDTO.getProduct_id() + " não encontrado", HttpStatus.NOT_FOUND);
//        }
//
//        Stock newStock = new Stock();
//        newStock.setProduct(this.productService.findById(stockDTO.getProduct_id()));
//    }


}
