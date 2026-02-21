package com.zest.products.service;

import com.zest.products.dto.ProductRequest;
import com.zest.products.dto.ProductResponse;
import com.zest.products.entity.Product;
import com.zest.products.exception.ResourceNotFoundException;
import com.zest.products.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(this::toResponse);
    }

    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return toResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request, String username) {
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setCreatedBy(username);
        product.setCreatedOn(LocalDateTime.now());

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    public ProductResponse updateProduct(Integer id, ProductRequest request, String username) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setProductName(request.getProductName());
        product.setModifiedBy(username);
        product.setModifiedOn(LocalDateTime.now());

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setProductName(product.getProductName());
        response.setCreatedBy(product.getCreatedBy());
        response.setCreatedOn(product.getCreatedOn());
        response.setModifiedBy(product.getModifiedBy());
        response.setModifiedOn(product.getModifiedOn());
        return response;
    }
}
