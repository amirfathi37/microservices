package com.example.productservice.mapper;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel ="spring")
public interface ProductMapper {
    ProductRequest product2ProductRequest(Product product);
    Product productRequest2Product(ProductRequest productRequest);


}
