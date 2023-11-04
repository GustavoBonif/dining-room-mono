package com.app.diningroom.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.app.diningroom.dto.ItemOrderDTO;
import com.app.diningroom.dto.OrdersDTO;
import com.app.diningroom.entities.ItemOrder;
import com.app.diningroom.entities.Orders;
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

//        return new ResponseEntity<>("Pedido criado com sucesso.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> update(Long id, OrdersDTO orderDTO) {
        Orders pedidoEntity = ordersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido with ID " + id + " not found."));

//        pedidoEntity.setItensPedido(itemPedidoService.criarItensPedido(pedidoDTO.getItensPedido(), pedidoEntity));
//        pedidoEntity.setPago(pedidoDTO.isPago());
        pedidoEntity.setPaymentMethod(orderDTO.getPaymentMethod());

        ordersRepository.save(pedidoEntity);

        return new ResponseEntity<>("Item do Pedido atualizado com sucesso.", HttpStatus.OK);
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

        // Converte os itens para DTOs se necessário
        return itensPedido.stream()
                .map(ItemOrderDTO::new)  // Suponhamos que você tenha um construtor em ItemPedidoDTO que aceita ItemPedidoEntity
                .collect(Collectors.toList());
    }

    @Transactional
    public Orders findEntityById(Long id) {
        return ordersRepository.findById(id).get();
    }

    public OrdersDTO entityToDTO(Orders order) {
        return new OrdersDTO(order);
    }

}
