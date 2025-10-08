package com.example.demoe.service.implementation;

import com.example.demoe.cache.ProductCacheService;
import com.example.demoe.dto.ProductDto;
import com.example.demoe.exception.ResourceNotFoundException;
import com.example.demoe.mapper.ProductMapper;
import com.example.demoe.model.Product;
import com.example.demoe.repository.ProductRepository;
import com.example.demoe.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductCacheService productCacheService;

    public ProductServiceImplementation(ProductRepository productRepository,
                                        ProductMapper productMapper,
                                        ProductCacheService productCacheService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productCacheService = productCacheService;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Request for creating product with name {}", productDto.getName());
        Product product = productMapper.toEntity(productDto);
        Product saved = productRepository.save(product);
        ProductDto dto = productMapper.toDto(saved);
        productCacheService.put(dto);
        return dto;
    }

    @Override
    public List<ProductDto> createProductList(List<ProductDto> productDtoList) {
        log.info("Received request to create {} product", productDtoList.size());
        List<Product> productList = productMapper.toEntityList(productDtoList);
        List<Product> savedList = productRepository.saveAll(productList);
        List<ProductDto> dtoList = productMapper.toDtoList(savedList);
        dtoList.forEach(productCacheService::put);
        return dtoList;
    }

    @Override
    public ProductDto getProductById(Long id) {
        log.info("Checking cache for product id {}", id);
        ProductDto cached = productCacheService.get(id);
        if (cached != null) {
            return cached;
        }

        log.debug("Cache hit for product id {}, querying db", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found in db for id {}", id);
                    return new ResourceNotFoundException("Product not found");
                });

        ProductDto dto = productMapper.toDto(product);

        productCacheService.put(dto);
        return dto;
    }

    @Override
    public List<ProductDto> getAllProduct() {
        List<Product> product = productRepository.findAll();

        List<ProductDto> dto = product.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        dto.forEach(productCacheService::put);

        return dto;
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product not found"));
        product.setName(productDto.getName());
        product.setSku(productDto.getSku());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        Product updated = productRepository.save(product);
        ProductDto dto = productMapper.toDto(updated);
        productCacheService.put(dto);
        return dto;
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("product not found"));
        productRepository.deleteById(id);
        productCacheService.delete(id);
    }

    @Override
    public void productCacheSync() {
        List<Product> products = productRepository.findAll();

        List<ProductDto> dto = products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        productCacheService.putAll(dto);
    }

}
