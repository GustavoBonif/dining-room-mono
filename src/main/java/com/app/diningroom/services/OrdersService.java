package com.app.diningroom.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.app.diningroom.dto.ItemOrderDTO;
import com.app.diningroom.dto.OrdersDTO;
import com.app.diningroom.entities.ItemOrder;
import com.app.diningroom.entities.Orders;
import com.app.diningroom.enums.PaymentMethod;
import com.app.diningroom.repositories.ItemOrderRepository;
import com.app.diningroom.repositories.OrdersRepository;

@Service
public class OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ItemOrderRepository itemOrderRepository;
    @Autowired
    private ClientService clientService;

    public ResponseEntity<OrdersDTO> findById(Long id) {
        Optional<Orders> optionalOrder = ordersRepository.findById(id);

        if (optionalOrder.isPresent()) {
            Orders entity = optionalOrder.get();
            OrdersDTO dto = new OrdersDTO(entity);
            dto.setItemsOrder(this.getListItemOrderDTO(entity));
            OrdersDTO orderFound = dto;
            return new ResponseEntity<>(orderFound, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public List<OrdersDTO> listAll() {
        List<Orders> orders = ordersRepository.findAll();
        return orders.stream()
                .map(this::convertToOrdersDTO)
                .collect(Collectors.toList());
    }

    private OrdersDTO convertToOrdersDTO(Orders order) {
        OrdersDTO ordersDTO = new OrdersDTO(order);

        List<ItemOrder> itemsOrder = itemOrderRepository.findByOrdersId(order.getId());

        List<ItemOrderDTO> itemOrderDTOs = itemsOrder.stream()
                .map(ItemOrderDTO::new)
                .collect(Collectors.toList());

        ordersDTO.setItemsOrder(itemOrderDTOs);

        return ordersDTO;
    }

    @Transactional
    public Long create(OrdersDTO ordersDTO) {
        Orders newOrders = new Orders();
        newOrders.setPaymentMethod(ordersDTO.getPaymentMethod());
        newOrders.setDateTime(ordersDTO.getDateTime());
        newOrders.setTotalPrice(ordersDTO.getTotalPrice());
        newOrders.setClient(clientService.findEntityById(ordersDTO.getClient_id()));

        Orders newOrder = ordersRepository.save(newOrders);

        return newOrder.getId();
    }

    @Transactional
    public ResponseEntity<String> delete(Long id) {
        Optional<Orders> optionalOrder = ordersRepository.findById(id);

        if (optionalOrder.isPresent()) {
            this.ordersRepository.deleteById(id);
            return new ResponseEntity<>("Pedido deletado com sucesso.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Pedido com ID " + id + " não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public OrdersDTO findByClientIdAndPaid(Long clientId) {
        List<Orders> orders = ordersRepository.findByClientIdAndPaid(clientId, false);

        if (orders.isEmpty()) {
            return null;
        }

        return new OrdersDTO(orders.get(0));
    }

    public List<ItemOrderDTO> getListItemOrderDTO(Orders order) {

        List<ItemOrder> itensPedido = order.getItemsOrder();

        return itensPedido.stream()
                .map(ItemOrderDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addItemOrderinOrder(Long orderId, ItemOrder newItemOrder) {
        Orders order = ordersRepository.findById(orderId).get();
        BigDecimal orderCurrentTotalPrice = order.getTotalPrice();
        BigDecimal newItemOrderSubTotalPrice = newItemOrder.getSubTotalPrice();
        order.setTotalPrice(orderCurrentTotalPrice.add(newItemOrderSubTotalPrice));
        order.setDateTime(LocalDateTime.now());

        ordersRepository.save(order);
    }

    @Transactional
    public ResponseEntity<String> deleteItemOrderFromOrder(ItemOrder itemOrder) {
        Orders order = itemOrder.getOrders();

        if (order == null || !ordersRepository.existsById(order.getId())) {
            return new ResponseEntity<>("Pedido não encontrado.", HttpStatus.NOT_FOUND);
        }

        BigDecimal orderCurrentTotalPrice = order.getTotalPrice();
        BigDecimal itemOrderSubTotalPrice = itemOrder.getSubTotalPrice();
        BigDecimal newTotalPrice = orderCurrentTotalPrice.subtract(itemOrderSubTotalPrice);
        order.setTotalPrice(newTotalPrice);

        ordersRepository.save(order);
        return ResponseEntity.ok("ItemOrder removido com sucesso.");
    }

    @Transactional
    public void updateItemOrderInOrder(ItemOrder itemOrderToUpdate) {
        Orders order = itemOrderToUpdate.getOrders();

        if (order != null || ordersRepository.existsById(order.getId())) {
            order.setTotalPrice(itemOrderToUpdate.getSubTotalPrice());
        }

        BigDecimal newTotalPrice = this.calculateTotalSubTotalPrice(order.getId());
        order.setTotalPrice(newTotalPrice);
    }

    public BigDecimal calculateTotalSubTotalPrice(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + orderId));

        BigDecimal totalSubTotalPrice = BigDecimal.ZERO;

        for (ItemOrder itemOrder : order.getItemsOrder()) {
            totalSubTotalPrice = totalSubTotalPrice.add(itemOrder.getSubTotalPrice());
        }

        return totalSubTotalPrice;
    }

    @Transactional
    public Orders findEntityById(Long id) {
        return ordersRepository.findById(id).get();
    }

    public OrdersDTO entityToDTO(Orders order) {
        return new OrdersDTO(order);
    }

    @Transactional
    public ResponseEntity<String> payOrder(Long orderId, PaymentMethod paymentMethod) {

        if(!ordersRepository.existsById(orderId)) {
            return new ResponseEntity<>("Pedido não encontrado com ID: " + orderId, HttpStatus.NOT_FOUND);
        }

        Orders order = ordersRepository.findById(orderId).get();
        if(order.isPaid()) {
            return new ResponseEntity<>("Pedido já foi pago.", HttpStatus.BAD_REQUEST);
        }

        order.setPaymentMethod(paymentMethod);
        order.setPaid(true);
        ordersRepository.save(order);

        return ResponseEntity.ok("Pedido pago com sucesso.");
    }
}