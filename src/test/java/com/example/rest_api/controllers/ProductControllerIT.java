package com.example.rest_api.controllers;

import com.example.rest_api.dtos.ProductRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Executes a POST request")
    public void test_createProduct_success() throws Exception {
        ProductRequestDTO product = new ProductRequestDTO("Name", "Description", new BigDecimal(1));
        String productJSON = mapper.writeValueAsString(product);

        mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.price").value(1));

    }







}
