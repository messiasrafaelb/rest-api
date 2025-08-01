package com.example.rest_api.controllers;

import com.example.rest_api.dtos.ProductRequestDTO;
import com.example.rest_api.dtos.ProductResponseDTO;
import com.example.rest_api.services.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        var productsList = service.getAllProducts();
        return ResponseEntity.ok(productsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable @Min(1) Long id) {
        var product = service.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequest) {
        return ResponseEntity.ok(service.createProduct(productRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable @Min(1) Long id,
                                                            @Valid @RequestBody ProductRequestDTO productRequest) {
        return ResponseEntity.ok(service.updateProduct(id, productRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable @Min(1) Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
