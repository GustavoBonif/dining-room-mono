package com.app.diningroom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.app.diningroom.dto.OrdersDTO;
import com.app.diningroom.services.OrdersService;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrdersService service;

    @Autowired
    public OrderController(OrdersService service) {
        this.service = service;
    }

    @GetMapping("/listAll")
    public List<OrdersDTO> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        ResponseEntity<OrdersDTO> response = service.findById(id);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response;
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Registro não encontrado");
        }
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody OrdersDTO ordersDTO) {
        Long newOrderId = service.create(ordersDTO);

        return new ResponseEntity<>("Sucesso ao criar o pedido de ID: " + newOrderId, HttpStatus.CREATED);
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
//        return service.update(id, orderDTO);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
