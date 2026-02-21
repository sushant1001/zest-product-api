package com.zest.products.service;

import com.zest.products.dto.ProductRequest;
import com.zest.products.dto.ProductResponse;
import com.zest.products.entity.Product;
import com.zest.products.exception.ResourceNotFoundException;
import com.zest.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1);
        sampleProduct.setProductName("Test Product");
        sampleProduct.setCreatedBy("admin");
        sampleProduct.setCreatedOn(LocalDateTime.now());
    }

    @Test
    void getProductById_shouldReturnProduct_whenProductExists() {
        when(productRepository.findById(1)).thenReturn(Optional.of(sampleProduct));

        ProductResponse response = productService.getProductById(1);

        assertNotNull(response);
        assertEquals("Test Product", response.getProductName());
        assertEquals("admin", response.getCreatedBy());
    }

    @Test
    void getProductById_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(99));
    }

    @Test
    void createProduct_shouldSaveAndReturnProduct() {
        ProductRequest request = new ProductRequest();
        request.setProductName("New Product");

        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse response = productService.createProduct(request, "admin");

        assertNotNull(response);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_shouldUpdateProduct_whenExists() {
        ProductRequest request = new ProductRequest();
        request.setProductName("Updated Name");

        when(productRepository.findById(1)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse response = productService.updateProduct(1, request, "admin");

        assertNotNull(response);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void deleteProduct_shouldDeleteProduct_whenExists() {
        when(productRepository.findById(1)).thenReturn(Optional.of(sampleProduct));
        doNothing().when(productRepository).delete(sampleProduct);

        assertDoesNotThrow(() -> productService.deleteProduct(1));
        verify(productRepository, times(1)).delete(sampleProduct);
    }

    @Test
    void deleteProduct_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(5)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(5));
    }
}
