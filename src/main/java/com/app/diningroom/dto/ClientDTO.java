package com.app.diningroom.dto;

import java.util.List;

import com.app.diningroom.entities.Client;
import com.app.diningroom.entities.Orders;

public class ClientDTO {

    private Long id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private List<ItemOrderDTO> itemsOrder;
    private List<Orders> orders;

    public ClientDTO(Long id, String name, String address, String email, String phone, List<ItemOrderDTO> itemsOrder, List<Orders> orders) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.itemsOrder = itemsOrder;
        this.orders = orders;
    }

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.address = client.getAddress();
        this.email = client.getEmail();
        this.phone = client.getPhone();
//        this.itemsOrder = client.getItemsOrder();
    }

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

    public List<ItemOrderDTO> getItemsOrder() {
        return itemsOrder;
    }

    public void setItemsOrder(List<ItemOrderDTO> itemsOrder) {
        this.itemsOrder = itemsOrder;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }
}
