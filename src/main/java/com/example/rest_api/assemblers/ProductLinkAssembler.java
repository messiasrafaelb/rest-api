package com.example.rest_api.assemblers;

import com.example.rest_api.controllers.ProductController;
import com.example.rest_api.dtos.ProductResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductLinkAssembler {

    public EntityModel<ProductResponseDTO> linkForGET(ProductResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ProductController.class).getProductById(dto.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).updateProduct(dto.getId(), null)).withRel("update").withType("PUT"),
                linkTo(methodOn(ProductController.class).deleteProduct(dto.getId())).withRel("delete").withType("DELETE"),
                linkTo(methodOn(ProductController.class).getAllProducts()).withRel("all").withType("GET"));
    }

    public EntityModel<ProductResponseDTO> linkForPOST(ProductResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ProductController.class).getProductById(dto.getId())).withSelfRel().withType("GET"));
    }

    public EntityModel<ProductResponseDTO> linkForPUT(ProductResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ProductController.class).getProductById(dto.getId())).withSelfRel().withType("GET"));
    }

    public CollectionModel<EntityModel<ProductResponseDTO>> linkForGETall(List<ProductResponseDTO> dtos) {
        var dtoList = dtos.stream().map(e -> EntityModel.of(e,
                        linkTo(methodOn(ProductController.class).getProductById(e.getId())).withSelfRel().withType("GET")))
                        .toList();
        return CollectionModel.of(dtoList);
    }

    public EntityModel<ProductResponseDTO> linkForDELETE(ProductResponseDTO dto) {
        return EntityModel.of(dto, linkTo(methodOn(ProductController.class).getAllProducts()).withRel("get-all").withType("GET"));
    }

}
