package com.example.rest_api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductResponseDTO {
    @JsonIgnore
    private Long id;
    private String name;
    private BigDecimal price;
}
