package com.app.diningroom.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.app.diningroom.dto.ItemOrderDTO;
import com.app.diningroom.dto.OrdersDTO;
import com.app.diningroom.entities.Client;
import com.app.diningroom.entities.ItemOrder;
import com.app.diningroom.entities.Orders;
import com.app.diningroom.entities.Product;
import com.app.diningroom.repositories.ItemOrderRepository;

@Service
public class ItemOrderService {

    @Autowired
    private ItemOrderRepository repository;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private ClientService clientService;

    public ResponseEntity<ItemOrderDTO> findById(Long id) {
        Optional<ItemOrder> optionalItemOrder = repository.findById(id);

        if (optionalItemOrder.isPresent()) {
            ItemOrder entity = optionalItemOrder.get();
            ItemOrderDTO dto = new ItemOrderDTO(entity);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public List<ItemOrderDTO>  listAll() {
        List<ItemOrder> itemOrders = this.repository.findAll();
        return itemOrders.stream()
                .map(this::itemOrderToItemOrderDTO)
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> mountNewItemOrder(ItemOrderDTO itemOrderDTOparam) {

        if(!this.productService.existsById(itemOrderDTOparam.getProduct_id())) {
            return new ResponseEntity<>("Produto de ID " + itemOrderDTOparam.getProduct_id() + " não encontrado.", HttpStatus.NOT_FOUND);
        }

        if(!this.clientService.existsById(itemOrderDTOparam.getClient_id())) {
            return new ResponseEntity<>("Cliente de ID " + itemOrderDTOparam.getProduct_id() + " não encontrado.", HttpStatus.NOT_FOUND);
        }

        Product product = productService.findEntityById(itemOrderDTOparam.getProduct_id());
        Client client = clientService.findEntityById(itemOrderDTOparam.getClient_id());

        ItemOrder newItemOrder = new ItemOrder();
        newItemOrder.setQuantity(itemOrderDTOparam.getQuantity());
        newItemOrder.setProduct(product);
        newItemOrder.setUnitPrice(product.getPrice());
        newItemOrder.calculateSubTotalPrice();
        newItemOrder.setClient(client);

        Orders order = this.createOrderIfNotExists(itemOrderDTOparam, newItemOrder);

        newItemOrder.setOrders(order);

        repository.save(newItemOrder);

        return new ResponseEntity<>("Item Pedido criado com sucesso.", HttpStatus.CREATED);
    }

    @Transactional
    public void create(ItemOrder itemOrder) {
        repository.save(itemOrder);
    }

    @Transactional
    public ResponseEntity<String> update(Long id, ItemOrderDTO itemOrderDTO) {
        if(!this.repository.existsById(id)) {
            return new ResponseEntity<>(" Pedido " + id + " não encontrado.", HttpStatus.NOT_FOUND);
        }

        if(itemOrderDTO.getProduct_id() != null && !this.productService.existsById(itemOrderDTO.getProduct_id())) {
            return new ResponseEntity<>("Produto de ID " + itemOrderDTO.getProduct_id() + " não encontrado.", HttpStatus.NOT_FOUND);
        }

        if(itemOrderDTO.getSubTotalPrice() != null) {
            return new ResponseEntity<>("Informe o preço unitário ou a quantidade para mudar o preço subtotal!", HttpStatus.FORBIDDEN);
        }

        if(itemOrderDTO.getUnitPrice() != null) {
            return new ResponseEntity<>("Preço unitário é indicado no Produto!", HttpStatus.FORBIDDEN);
        }

        ItemOrder itemOrderToUpdate = this.repository.findById(id).get();

        BeanUtils.copyProperties(itemOrderDTO, itemOrderToUpdate, this.getNullPropertyNames(itemOrderDTO));

        if(itemOrderDTO.getProduct_id() != null) {
            Product product = productService.findEntityById(itemOrderDTO.getProduct_id());
            itemOrderToUpdate.setProduct(product);
            itemOrderToUpdate.setUnitPrice(product.getPrice());

            if(itemOrderDTO.getQuantity() == 0) {
                itemOrderToUpdate.setQuantity(1);
            }
        }
        itemOrderToUpdate.calculateSubTotalPrice();

        ordersService.updateItemOrderInOrder(itemOrderToUpdate);

        this.repository.save(itemOrderToUpdate);

        return new ResponseEntity<>("Item do Pedido atualizado com sucesso.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> delete(Long id) {
        Optional<ItemOrder> optionalItemOrder = this.repository.findById(id);

        if (optionalItemOrder.isPresent()) {
            ItemOrder itemOrder = optionalItemOrder.get();

            ResponseEntity<String> responseOrder = ordersService.deleteItemOrderFromOrder(itemOrder);

            if (responseOrder.getStatusCode() == HttpStatus.NOT_FOUND) {
                return responseOrder;
            }

            this.repository.deleteById(id);
            return new ResponseEntity<>("Item do Pedido deletado com sucesso.", HttpStatus.OK);

        } else {
            return new ResponseEntity<>("Item do Pedido com ID " + id + " não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public Orders createOrderIfNotExists(ItemOrderDTO itemOrderDTOparam, ItemOrder newItemOrder) {
        OrdersDTO orderDTO = ordersService.findByClientIdAndPaid(itemOrderDTOparam.getClient_id());

        if(orderDTO == null) {
            OrdersDTO newOrderDTO = new OrdersDTO();
            newOrderDTO.setPaid(false);
            newOrderDTO.setDateTime(LocalDateTime.now());
            newOrderDTO.setClient_id(itemOrderDTOparam.getClient_id());
            newOrderDTO.setTotalPrice(newItemOrder.getSubTotalPrice());

            ItemOrderDTO newItemOrderDTO = new ItemOrderDTO();
            newItemOrderDTO.setProduct_id(newItemOrderDTO.getProduct_id());
            newItemOrderDTO.setQuantity(newItemOrder.getQuantity());
            newItemOrderDTO.setUnitPrice(newItemOrder.getUnitPrice());
            newItemOrderDTO.setSubTotalPrice(newItemOrder.getSubTotalPrice());
            newItemOrderDTO.setClient_id(newItemOrder.getClient().getId());

            List<ItemOrderDTO> newList = new ArrayList<>();
            newList.add(newItemOrderDTO);
            newOrderDTO.setItemsOrder(newList);

            Long newOrderId = ordersService.create(newOrderDTO);

            Orders newOrder = ordersService.findEntityById(newOrderId);
            orderDTO = ordersService.entityToDTO(newOrder);
        } else {
            ordersService.addItemOrderinOrder(orderDTO.getId(), newItemOrder);
        }

        return ordersService.findEntityById(orderDTO.getId());
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private ItemOrderDTO itemOrderToItemOrderDTO(ItemOrder itemOrder) {
        return new ItemOrderDTO(itemOrder);
    }

}
