package com.example.rest_api.controllers;

import com.example.rest_api.dtos.ProductRequestDTO;
import com.example.rest_api.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.not;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Sql("/scripts/products_insert.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    ProductService service;

    private String URI = "/api/products";

    @Test
    @DisplayName("Should return 202 when create new product")
    public void test_createProduct_success() throws Exception {
        ProductRequestDTO product = new ProductRequestDTO("Name", "Description", new BigDecimal(1));
        String productJSON = mapper.writeValueAsString(product);
        mvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.price").value(1));
    }

    private static Stream<ProductRequestDTO> invalidProducts() {
        return Stream.of(
                new ProductRequestDTO("", "Description", new BigDecimal(1)),
                new ProductRequestDTO("Name", "", new BigDecimal(1)),
                new ProductRequestDTO("Name", "Description", null)
        );
    }
    @MethodSource("invalidProducts")
    @ParameterizedTest(name = "Should return 400 when name is empty")
    public void test_createProduct_failure(ProductRequestDTO request) throws Exception {
        mvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    @DisplayName("Should return 200 and a list of products")
    public void test_getAllProducts_success() throws Exception {
        mvc.perform(get(URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.products", not(empty())).exists());
    }

    @Test
    @DisplayName("Should return 200 when getting a product by id")
    public void test_getProductById_success() throws Exception {
        mvc.perform(get(URI.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 400 when getting a product by id zero")
    public void test_getProductById_failure() throws Exception {
        mvc.perform(get(URI.concat("/0"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 when getting a product not found")
    public void test_getProductById_failure_404() throws Exception {
        mvc.perform(get(URI.concat("/999"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("Should return 200 when update a product")
    public void test_updateProduct_success() throws Exception {
        ProductRequestDTO product = new ProductRequestDTO("Name", "Test", new BigDecimal(1));
        String productJSON = mapper.writeValueAsString(product);
        mvc.perform(put(URI.concat("/5"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.price").value(1));
    }

    @Test
    @DisplayName("Should return 400 when update a product with invalid entry")
    public void test_updateProduct_failure() throws Exception {
        ProductRequestDTO product = new ProductRequestDTO("", "Test", null);
        String productJSON = mapper.writeValueAsString(product);
        mvc.perform(put(URI.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 when update a product with invalid id")
    public void test_updateProduct_failure_id() throws Exception {
        ProductRequestDTO product = new ProductRequestDTO("Name", "Test", new BigDecimal(1));
        String productJSON = mapper.writeValueAsString(product);
        mvc.perform(put(URI.concat("/999"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 204 when delete a product")
    public void test_deleteProduct_success() throws Exception {
        mvc.perform(delete(URI.concat("/3"))).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 when delete a product with invalid id")
    public void test_deleteProduct_failure() throws Exception {
        mvc.perform(delete(URI.concat("/999"))).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when JSON body is invalid")
    public void test_createProduct_badRequest_invalidJson() throws Exception {
        String invalidJson = "{\"name\": \"Produto\", \"price\": 10,,}";
        mvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Invalid format in request body. Please check your JSON syntax"));
    }


}
