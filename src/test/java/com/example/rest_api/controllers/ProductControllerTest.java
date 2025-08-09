package com.example.rest_api.controllers;

import com.example.rest_api.exceptions.GlobalExceptionHandler;
import com.example.rest_api.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    private String URI = "/api/products";

    private MockMvc mvc;

    @Mock
    private ProductService service;

    @InjectMocks
    private ProductController controller;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()) // para testar o handler também
                .build();
    }

    @Test
    @DisplayName("Should return 500 on unexpected exception")
    public void test_genericException() throws Exception {
        when(service.getAllProducts())
                .thenThrow(new RuntimeException());
        mvc.perform(get(URI))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }

}
