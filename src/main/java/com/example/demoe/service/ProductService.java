package com.example.demoe.service;

import com.example.demoe.dto.ProductDto;

import java.util.List;

public interface ProductService {


    ProductDto createProduct(ProductDto productDto);

    List<ProductDto> createProductList(List<ProductDto> productDtoList);

    ProductDto getProductById(Long id);

    List<ProductDto> getAllProduct();

    ProductDto updateProduct(Long id, ProductDto productDto);

    void deleteProduct(Long id);

    void productCacheSync();
}
