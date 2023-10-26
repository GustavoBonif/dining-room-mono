package com.app.diningroom.dto;

import com.app.diningroom.entities.Stock;

public class StockDTO {

    private Long id;

    private Long product_id;

    private int quantityAvailable;

    public StockDTO(Long id, Long product_id, int quantityAvailable) {
        this.id = id;
        this.product_id = product_id;
        this.quantityAvailable = quantityAvailable;
    }

    public StockDTO(Stock stock) {
        this.id = stock.getId();
        this.product_id = stock.getProduct().getId();
        this.quantityAvailable = stock.getQuantityAvailable();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }
}
