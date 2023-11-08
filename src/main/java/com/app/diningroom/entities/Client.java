package com.app.diningroom.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String address;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ItemOrder> itemsOrder;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Orders> orders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<ItemOrder> getItemsOrder() {
        return itemsOrder;
    }

    public void setItemsOrder(List<ItemOrder> itemsOrder) {
        this.itemsOrder = itemsOrder;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }
}
