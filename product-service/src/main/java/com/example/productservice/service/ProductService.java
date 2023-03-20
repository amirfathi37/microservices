package com.example.productservice.service;

import com.example.productservice.repository.ProductRepo;
import org.mapstruct.factory.Mappers;
import com.example.productservice.dto.ProductRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepo productRepo;

    public void createProduct(ProductRequest productRequest) {
        ProductMapper mapper = Mappers.getMapper(ProductMapper.class);
        Product product = mapper.productRequest2Product(productRequest);
        Product saved = productRepo.save(product);
        log.info("has been saved product with id : {}", product.getId());
    }

    public List<ProductRequest> getAllProducts() {
        List<Product> products = productRepo.findAll();
        return products.stream().map(productMapper::product2ProductRequest).collect(Collectors.toList());
    }
}
