package com.example.rest_api.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "{product.name.notblank}")
    @Size(max = 100, message = "{product.name.size}")
    private String name;

    @NotBlank(message = "{product.description.notblank}")
    @Size(max = 300, message = "{product.description.size}")
    private String description;

    @Positive(message = "{product.price.positive}")
    @Digits(integer = 9, fraction = 2, message = "{product.price.digits}")
    private BigDecimal price;
}

