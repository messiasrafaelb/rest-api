package com.example.rest_api.mappers;

import com.example.rest_api.dtos.ProductRequestDTO;
import com.example.rest_api.dtos.ProductResponseDTO;
import com.example.rest_api.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product requestToEntity(ProductRequestDTO requestDTO);
    ProductResponseDTO entityToResponse(Product product);
}
