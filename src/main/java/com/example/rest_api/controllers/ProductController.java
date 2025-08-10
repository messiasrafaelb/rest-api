package com.example.rest_api.controllers;

import com.example.rest_api.assemblers.ProductLinkAssembler;
import com.example.rest_api.dtos.ProductDeleteResponseDTO;
import com.example.rest_api.dtos.ProductRequestDTO;
import com.example.rest_api.dtos.ProductResponseDTO;
import com.example.rest_api.services.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;
    private final ProductLinkAssembler link;

    @Autowired
    public ProductController(ProductService service, ProductLinkAssembler link) {
        this.service = service;
        this.link = link;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProductResponseDTO>>> getAllProducts() {
        var products = service.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK)
                .body(link.linkForGETall(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductResponseDTO>> getProductById(@PathVariable
                                                             @Min(value = 1, message = "{product.id.min}") long id) {
        var product = service.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(link.linkForGET(product));
    }

    @PostMapping
    public ResponseEntity<EntityModel<ProductResponseDTO>> createProduct(@Valid @RequestBody ProductRequestDTO productRequest) {
        var product = service.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(link.linkForPOST(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProductResponseDTO>> updateProduct(@PathVariable
                                                            @Min(value = 1, message = "{product.id.min}") long id,
                                                            @Valid @RequestBody ProductRequestDTO productRequest) {
        var product = service.updateProduct(id, productRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(link.linkForPUT(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<ProductDeleteResponseDTO>> deleteProduct(@PathVariable
                                              @Min(value = 1, message = "{product.id.min}") long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(link.linkForDELETE(service.deleteProduct(id)));
    }
}
