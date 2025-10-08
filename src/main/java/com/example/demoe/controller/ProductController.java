package com.example.demoe.controller;

import com.example.demoe.dto.ProductDto;
import com.example.demoe.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        ProductDto created = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ProductDto>> createProductList(@Valid @RequestBody List<@Valid ProductDto> productDtoList) {
        List<ProductDto> createdList = productService.createProductList(productDtoList);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        log.info("Received request to get product with id {}", id);
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProduct() {
        List<ProductDto> product = productService.getAllProduct();
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updatedProduct(@PathVariable Long id,
                                                     @Valid @RequestBody ProductDto productDto) {
        ProductDto updated = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
         return ResponseEntity.noContent().build();
    }

}
