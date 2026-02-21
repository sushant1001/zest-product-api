package com.zest.products.service;

import com.zest.products.dto.ItemRequest;
import com.zest.products.dto.ItemResponse;
import com.zest.products.entity.Item;
import com.zest.products.entity.Product;
import com.zest.products.exception.ResourceNotFoundException;
import com.zest.products.repository.ItemRepository;
import com.zest.products.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ProductRepository productRepository;

    public ItemService(ItemRepository itemRepository, ProductRepository productRepository) {
        this.itemRepository = itemRepository;
        this.productRepository = productRepository;
    }

    public List<ItemResponse> getItemsByProductId(Integer productId) {

        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<Item> items = itemRepository.findByProductId(productId);
        return items.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ItemResponse addItemToProduct(Integer productId, ItemRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Item item = new Item();
        item.setQuantity(request.getQuantity());
        item.setProduct(product);

        Item saved = itemRepository.save(item);
        return toResponse(saved);
    }

    private ItemResponse toResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setQuantity(item.getQuantity());
        response.setProductId(item.getProduct().getId());
        return response;
    }
}
