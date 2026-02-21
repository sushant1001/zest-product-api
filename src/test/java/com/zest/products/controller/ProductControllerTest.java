package com.zest.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zest.products.dto.ProductRequest;
import com.zest.products.dto.ProductResponse;
import com.zest.products.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private ProductResponse buildSampleResponse() {
        ProductResponse r = new ProductResponse();
        r.setId(1);
        r.setProductName("Sample Product");
        r.setCreatedBy("admin");
        r.setCreatedOn(LocalDateTime.now());
        return r;
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllProducts_shouldReturn200() throws Exception {
        when(productService.getAllProducts(any())).thenReturn(
                new PageImpl<>(List.of(buildSampleResponse()), PageRequest.of(0, 10), 1)
        );

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getProductById_shouldReturn200_whenExists() throws Exception {
        when(productService.getProductById(1)).thenReturn(buildSampleResponse());

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.productName").value("Sample Product"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createProduct_shouldReturn201() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setProductName("New Product");

        when(productService.createProduct(any(), anyString())).thenReturn(buildSampleResponse());

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void createProduct_shouldReturn403_whenNotAdmin() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setProductName("New Product");

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createProduct_shouldReturn400_whenNameIsBlank() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setProductName("");

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
