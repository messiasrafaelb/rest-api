package com.example.rest_api.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @ToString.Include
    @Column(nullable = false)
    private String title;

    @NonNull
    @ToString.Include
    @Column(nullable = false)
    private String author;

    @NonNull
    @Column(nullable = false, unique = true)
    private String isbn;

}
