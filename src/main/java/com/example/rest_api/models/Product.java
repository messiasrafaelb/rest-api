package com.example.rest_api.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ToString.Include
    @Column(nullable = false)
    private String name;

    @ToString.Include
    @Column(length = 150)
    private String description;

    @NonNull
    @ToString.Include
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
