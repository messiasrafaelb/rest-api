package com.example.rest_api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Relation(collectionRelation = "products")
public class ProductResponseDTO {
    @JsonIgnore
    private Long id;
    private String name;
    private BigDecimal price;
}
