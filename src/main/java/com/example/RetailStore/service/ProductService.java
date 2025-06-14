package com.example.RetailStore.service;


import com.example.RetailStore.dto.request.ProductRequest;
import com.example.RetailStore.dto.response.ProductResponse;
import com.example.RetailStore.entity.Product;
import com.example.RetailStore.exception.AppException;
import com.example.RetailStore.exception.ErrorCode;
import com.example.RetailStore.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    ModelMapper modelMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public void createProduct(ProductRequest request) {
        Product product = modelMapper.map(request, Product.class);
        productRepository.save(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updateProduct(String productId, ProductRequest request) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
        );
        modelMapper.map(request, product);
        productRepository.save(product);
    }

    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
        );
        return modelMapper.map(product, ProductResponse.class);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(p -> modelMapper.map(p, ProductResponse.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
        );
        productRepository.delete(product);
    }
}
