package com.app.diningroom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.app.diningroom.dto.StockDTO;
import com.app.diningroom.services.StockService;

@RestController
@RequestMapping("/stocks")
public class StockController {
    private StockService service;

    @Autowired
    public StockController(StockService stockService) {
        this.service = stockService;
    }

    @GetMapping("/listAll")
    public List<StockDTO> listAllStocks() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<String> createStock(@RequestBody StockDTO stockDTO) {
        return service.mountNewStock(stockDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateStock(@PathVariable Long id, @RequestBody StockDTO stockDTO) {
        return service.updateStock(id, stockDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStock(@PathVariable Long id) {
        return service.deleteStock(id);
    }
}
