package com.app.diningroom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.app.diningroom.dto.ItemOrderDTO;
import com.app.diningroom.services.ItemOrderService;

@RestController
@RequestMapping("/itemsOrder")
public class ItemOrderController {
    private ItemOrderService service;

    @Autowired
    public ItemOrderController(ItemOrderService service) {
        this.service = service;
    }

    @GetMapping("/listAll")
    public List<ItemOrderDTO> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemOrderDTO> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody ItemOrderDTO itemOrderDTO) {
        return service.create(itemOrderDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody ItemOrderDTO itemOrderDTO) {
        return service.update(id, itemOrderDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}

