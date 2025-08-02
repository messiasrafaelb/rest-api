package com.example.rest_api.services;

import com.example.rest_api.dtos.ProductRequestDTO;
import com.example.rest_api.dtos.ProductResponseDTO;
import com.example.rest_api.exceptions.ProductNotFoundException;
import com.example.rest_api.mappers.ProductMapper;
import com.example.rest_api.models.Product;
import com.example.rest_api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Autowired
    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ProductResponseDTO> getAllProducts() {
        return repository.findAll().stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO getProductById(Long id) {
        return mapper.entityToResponse(repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id)));
    }

    public ProductResponseDTO createProduct(ProductRequestDTO productRequest) {
        return mapper.entityToResponse(repository.save(mapper.requestToEntity(productRequest)));
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequest) {
        var product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        Product savedProduct = repository.save(product);
        return mapper.entityToResponse(savedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        repository.delete(product);
    }

}
