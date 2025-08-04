package com.example.rest_api.controllers;

import com.example.rest_api.dtos.ProductRequestDTO;
import com.example.rest_api.dtos.ProductResponseDTO;
import com.example.rest_api.services.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<EntityModel<ProductResponseDTO>> getProductById(@PathVariable
                                                             @Min(value = 1, message = "{product.id.min}") Long id) {
        var product = service.getProductById(id);
        var model = EntityModel.of(product);
        model.add(linkTo(methodOn(ProductController.class).getProductById(id)).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("all-products"));
        model.add(linkTo(methodOn(ProductController.class).deleteProduct(id)).withRel("delete"));
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequest) {
        return ResponseEntity.ok(service.createProduct(productRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable
                                                            @Min(value = 1, message = "{product.id.min}") Long id,
                                                            @Valid @RequestBody ProductRequestDTO productRequest) {
        return ResponseEntity.ok(service.updateProduct(id, productRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable
                                              @Min(value = 1, message = "{product.id.min}") Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
