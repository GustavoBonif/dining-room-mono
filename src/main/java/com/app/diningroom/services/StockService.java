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

import com.app.diningroom.dto.StockDTO;
import com.app.diningroom.entities.Stock;
import com.app.diningroom.repositories.StockRepository;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ProductService productService;

    @Transactional
    public ResponseEntity<StockDTO> findById(Long id) {
        Stock entity = stockRepository.findById(id).get();
        StockDTO dto = new StockDTO(entity);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Transactional
    public List<StockDTO> listAll() {
        List<Stock> stocks = this.stockRepository.findAll();

        return stocks.stream()
                .map(this::stockToStockDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseEntity<String> createStock(StockDTO stockDTO) {
        if(!this.productService.existsById(stockDTO.getProduct_id())) {
            return new ResponseEntity<>("Produto com ID " + stockDTO.getProduct_id() + " não encontrado", HttpStatus.NOT_FOUND);
        }

        Stock newStock = new Stock();
        newStock.setProduct(productService.findEntityById(stockDTO.getProduct_id()));
        newStock.setQuantityAvailable(stockDTO.getQuantityAvailable());

        this.stockRepository.save(newStock);

        return new ResponseEntity<>("Estoque criado com sucesso.", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> updateStock(Long id, StockDTO stockDTO) {
        if (!stockRepository.existsById(id)) {
            return new ResponseEntity<>("Estoque com o id " + id + " não existe.", HttpStatus.NOT_FOUND);
        }
        Stock stockToUpdate = stockRepository.findById(id).get();

        BeanUtils.copyProperties(stockDTO, stockToUpdate, this.getNullPropertyNames(stockDTO));

        this.stockRepository.save(stockToUpdate);

        return new ResponseEntity<>("Estoque atualizado com sucesso.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deleteStock(Long id) {
        Optional<Stock> optionalStock = stockRepository.findById(id);

        if (optionalStock.isPresent()) {
            stockRepository.deleteById(id);
            return new ResponseEntity<>("Estoque deletado com sucesso.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Estoque com ID " + id + " não encontrado.", HttpStatus.NOT_FOUND);
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

    private StockDTO stockToStockDTO(Stock stock) {
        return new StockDTO(stock);
    }

    public void updateStockByProduct(Long productId, int quantityOrdered) {
        Stock stock = stockRepository.findByProductId(productId);

        if (stock != null) {
            int currentQuantityAvailable = stock.getQuantityAvailable();
            int updatedQuantityAvailable = currentQuantityAvailable - quantityOrdered;

            if (updatedQuantityAvailable >= 0) {
                stock.setQuantityAvailable(updatedQuantityAvailable);
                stockRepository.save(stock);
            } else {
                throw new IllegalArgumentException("Quantidade insuficiente em estoque para o produto com ID " + productId);
            }
        } else {
            throw new IllegalArgumentException("Produto com ID " + productId + " não encontrado em estoque");
        }
    }
}
