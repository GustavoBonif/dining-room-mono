package com.app.diningroom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stocks")
public class StockController {
    private StockService service;

    @Autowired
    public StockController(StockService stockService) {
        this.service = stockService;
    }


}
