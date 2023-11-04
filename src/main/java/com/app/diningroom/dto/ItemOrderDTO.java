package com.app.diningroom.dto;

import java.math.BigDecimal;

import com.app.diningroom.entities.ItemOrder;

public class ItemOrderDTO {

    private Long id;
    private Long product_id;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotalPrice;
    private Long orders_id;
    private Long client_id;

    public ItemOrderDTO(Long id, Long product_id, int quantity, Long client_id) {
        this.id = id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.client_id = client_id;
    }

    public ItemOrderDTO(ItemOrder itemOrders) {
        this.id = itemOrders.getId();
        this.product_id = itemOrders.getProduct().getId();
        this.quantity = itemOrders.getQuantity();
        this.unitPrice = itemOrders.getUnitPrice();
        this.subTotalPrice = itemOrders.getSubTotalPrice();
        this.client_id = itemOrders.getClient().getId();
        this.orders_id = itemOrders.getOrders().getId();
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

    public Long getOrders_id() {
        return orders_id;
    }

    public void setOrders_id(Long order_id) {
        this.orders_id = order_id;
    }

    public Long getClient_id() {
        return client_id;
    }

    public void setClient_id(Long client_id) {
        this.client_id = client_id;
    }
}
