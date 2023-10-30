package com.app.diningroom.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "itemOrder")
public class ItemOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int quantity;

//    @Column(nullable = false)
    private BigDecimal unitPrice;

    private BigDecimal subTotalPrice;

    public ItemOrder(Long id, Product product, int quantity, BigDecimal unitPrice, BigDecimal subTotalPrice) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotalPrice = subTotalPrice;
    }

    public ItemOrder() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public void calculateSubTotalPrice() {
        BigDecimal subTotalPrice = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));

        this.setSubTotalPrice(subTotalPrice);
    }
}
