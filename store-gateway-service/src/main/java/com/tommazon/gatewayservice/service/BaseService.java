package com.tommazon.gatewayservice.service;

import com.tommazon.gatewayservice.entity.Product;
import com.tommazon.gatewayservice.entity.respository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Data
public class BaseService {
    final ProductRepository productRepository;

    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public List<Product> getAllProductByEnabled(Boolean enabled) {
        return productRepository.findAllByEnabled(enabled);
    }

}
