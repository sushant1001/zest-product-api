package com.zest.products.controller;

import com.zest.products.dto.ApiResponse;
import com.zest.products.dto.ProductRequest;
import com.zest.products.dto.ProductResponse;
import com.zest.products.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Product management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all products with pagination")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Products fetched successfully", products));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Integer id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product fetched successfully", product));
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        ProductResponse created = productService.createProduct(request, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Product created successfully", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        ProductResponse updated = productService.updateProduct(id, request, username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product deleted successfully", null));
    }
}
