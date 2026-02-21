package com.zest.products.controller;

import com.zest.products.dto.ApiResponse;
import com.zest.products.dto.ItemRequest;
import com.zest.products.dto.ItemResponse;
import com.zest.products.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Items", description = "Item endpoints under products")
@SecurityRequirement(name = "bearerAuth")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{productId}/items")
    @Operation(summary = "Get all items for a product")
    public ResponseEntity<ApiResponse<List<ItemResponse>>> getItemsByProduct(@PathVariable Integer productId) {
        List<ItemResponse> items = itemService.getItemsByProductId(productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Items fetched successfully", items));
    }

    @PostMapping("/{productId}/items")
    @Operation(summary = "Add item to a product")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ItemResponse>> addItem(
            @PathVariable Integer productId,
            @Valid @RequestBody ItemRequest request) {

        ItemResponse item = itemService.addItemToProduct(productId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Item added successfully", item));
    }
}
