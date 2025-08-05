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
    private final ProductLinkAssembler link;

    @Autowired
    public ProductController(ProductService service, ProductLinkAssembler link) {
        this.service = service;
        this.link = link;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProductResponseDTO>>> getAllProducts() {
        var productsList = service.getAllProducts();
        return ResponseEntity.ok(link.linkForGETall(productsList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductResponseDTO>> getProductById(@PathVariable
                                                             @Min(value = 1, message = "{product.id.min}") long id) {
        var product = service.getProductById(id);
        return ResponseEntity.ok(link.linkForGET(product));
    }

    @PostMapping
    public ResponseEntity<EntityModel<ProductResponseDTO>> createProduct(@Valid @RequestBody ProductRequestDTO productRequest) {
        return ResponseEntity.ok(link.linkForPOST(service.createProduct(productRequest)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProductResponseDTO>> updateProduct(@PathVariable
                                                            @Min(value = 1, message = "{product.id.min}") long id,
                                                            @Valid @RequestBody ProductRequestDTO productRequest) {
        ProductResponseDTO product = service.updateProduct(id, productRequest);
        return ResponseEntity.ok(link.linkForPUT(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<ProductDeleteResponseDTO>> deleteProduct(@PathVariable
                                              @Min(value = 1, message = "{product.id.min}") long id) {
        service.deleteProduct(id);
        return ResponseEntity.ok(link.linkForDELETE(new ProductDeleteResponseDTO("Product deleted")));
    }
}
