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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.app.diningroom.dto.ItemOrderDTO;
import com.app.diningroom.entities.ItemOrder;
import com.app.diningroom.entities.Product;
import com.app.diningroom.repositories.ItemOrderRepository;

@Service
public class ItemOrderService {

    @Autowired
    private ItemOrderRepository repository;
    @Autowired
    private ProductService productService;

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

    @Transactional
    public ResponseEntity<String> create(ItemOrderDTO itemOrderDTO) {

        if(!this.productService.existsById(itemOrderDTO.getProduct_id())) {
            return new ResponseEntity<>("Produto de ID " + itemOrderDTO.getProduct_id() + " não encontrado.", HttpStatus.NOT_FOUND);
        }

        Product product = productService.findEntityById(itemOrderDTO.getProduct_id());

        ItemOrder newItemOrder = new ItemOrder();
        newItemOrder.setQuantity(itemOrderDTO.getQuantity());
        newItemOrder.setProduct(product);
        newItemOrder.setUnitPrice(product.getPrice());

        newItemOrder.calculateSubTotalPrice();

//        // Adiciona itemOrder em Order
//        Order order = orderService.findOrderById(1L);
//        newItemOrder.setOrder(order);

        repository.save(newItemOrder);

        return new ResponseEntity<>("Estoque criado com sucesso.", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> update(Long id, ItemOrderDTO itemOrderDTO) {
        if(!this.repository.existsById(id)) {
            return new ResponseEntity<>("Item do Pedido " + id + " não encontrado.", HttpStatus.NOT_FOUND);
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
        this.repository.save(itemOrderToUpdate);

        return new ResponseEntity<>("Item do Pedido atualizado com sucesso.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> delete(Long id) {
        Optional<ItemOrder> optionalItemOrder = this.repository.findById(id);

        if (optionalItemOrder.isPresent()) {
            this.repository.deleteById(id);
            return new ResponseEntity<>("Item do Pedido deletado com sucesso.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Item do Pedido com ID " + id + " não encontrado.", HttpStatus.NOT_FOUND);
        }
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
