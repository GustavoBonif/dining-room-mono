package com.app.diningroom.dto;

import java.math.BigDecimal;

import com.app.diningroom.entities.ItemOrder;

public class ItemOrderDTO {

    private Long id;

    private Long product_id;

    private int quantity;

    private BigDecimal unitPrice;

    private BigDecimal subTotalPrice;

    public ItemOrderDTO(Long id, Long product_id, int quantity) {
        this.id = id;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public ItemOrderDTO(ItemOrder itemOrder) {
        this.id = itemOrder.getId();
        this.product_id = itemOrder.getProduct().getId();
        this.quantity = itemOrder.getQuantity();
        this.unitPrice = itemOrder.getUnitPrice();
        this.subTotalPrice = itemOrder.getSubTotalPrice();
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubTotalPrice() {
        return subTotalPrice;
    }

    public void setSubTotalPrice(BigDecimal subTotalPrice) {
        this.subTotalPrice = subTotalPrice;
    }
}
