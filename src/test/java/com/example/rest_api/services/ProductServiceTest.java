package com.example.rest_api.services;

import com.example.rest_api.dtos.ProductRequestDTO;
import com.example.rest_api.dtos.ProductResponseDTO;
import com.example.rest_api.exceptions.ProductNotFoundException;
import com.example.rest_api.mappers.ProductMapper;
import com.example.rest_api.models.Product;
import com.example.rest_api.repositories.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductMapper mapper;

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    @DisplayName("Should return a list of products")
    void test_getAllProducts_success() {
        //Arrange
        var mockEntity1 = new Product(1L, "Name1", "Description1", new BigDecimal(1));
        var mockEntity2 = new Product(2L, "Name2", "Description2", new BigDecimal(2));
        var mockDTO1 = new ProductResponseDTO(1L,"Name1",  new BigDecimal(1));
        var mockDTO2 = new ProductResponseDTO(2L, "Name2",  new BigDecimal(2));
        when(repository.findAll()).thenReturn(List.of(mockEntity1, mockEntity2));
        when(mapper.entityToResponse(mockEntity1)).thenReturn(mockDTO1);
        when(mapper.entityToResponse(mockEntity2)).thenReturn(mockDTO2);
        //Act
        List<ProductResponseDTO> result = service.getAllProducts();
        //Assert
        assertThat(result).containsExactly(mockDTO1, mockDTO2);
        //Verify
        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
        verify(mapper).entityToResponse(mockEntity1);
        verify(mapper).entityToResponse(mockEntity2);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("Should return a empty list when no products exist")
    void test_getAllProducts_edge() {
        //Arrange
        when(repository.findAll()).thenReturn(List.of());
        //Act
        List<ProductResponseDTO> result = service.getAllProducts();
        //Assert
        assertThat(result).isEmpty();
        //Verify
        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should return a product response")
    void test_getProductById_success() {
        //Arrange
        var mockEntity = new Product(1L, "Name", "Description", new BigDecimal(1));
        var mockDTO = new ProductResponseDTO(1L, "Name", new BigDecimal(1));
        when(repository.findById(1L)).thenReturn(Optional.of(mockEntity));
        when(mapper.entityToResponse(mockEntity)).thenReturn(mockDTO);
        //Act
        ProductResponseDTO result = service.getProductById(1L);
        //Assert
        assertThat(result).isEqualTo(mockDTO);
        //Verify
        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
        verify(mapper).entityToResponse(mockEntity);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product not found")
    void test_getProductById_failure() {
        //Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());
        //Act, Assert
        assertThrows(ProductNotFoundException.class, () -> service.getProductById(1L));
        //Verify
        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should return a ProductResponseDTO")
    void test_createProduct_success() {
        //Arrange
        var mockRequestDTO = new ProductRequestDTO("Name", "Description", new BigDecimal(1));
        var mockResponseDTO = new ProductResponseDTO(1L,"Name", new BigDecimal(1));
        var mockEntity = new Product(1L, "Name", "Description", new BigDecimal(1));
        when(repository.save(mockEntity)).thenReturn(mockEntity);
        when(mapper.entityToResponse(mockEntity)).thenReturn(mockResponseDTO);
        when(mapper.requestToEntity(mockRequestDTO)).thenReturn(mockEntity);
        //Act
        ProductResponseDTO result = service.createProduct(mockRequestDTO);
        //Assert
        assertThat(result).isEqualTo(mockResponseDTO);
        //Verify
        verify(repository).save(mockEntity);
        verifyNoMoreInteractions(repository);
        verify(mapper).requestToEntity(mockRequestDTO);
        verify(mapper).entityToResponse(mockEntity);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("Should return a ProductResponseDTO when updateProduct method is call")
    void test_updateProduct_success() {
        //Arrange
        var mockRequestDTO = new ProductRequestDTO("Name", "Description", new BigDecimal(1));
        var mockResponseDTO = new ProductResponseDTO(1L,"Name", new BigDecimal(1));
        var mockEntity = new Product(1L, "Name", "Description", new BigDecimal(1));
        when(repository.findById(1L)).thenReturn(Optional.of(mockEntity));
        when(repository.save(mockEntity)).thenReturn(mockEntity);
        when(mapper.entityToResponse(mockEntity)).thenReturn(mockResponseDTO);
        //Act
        ProductResponseDTO result = service.updateProduct(1L, mockRequestDTO);
        //Assert
        assertThat(result).isEqualTo(mockResponseDTO);
        //Verify
        verify(repository).findById(1L);
        verify(repository).save(mockEntity);
        verifyNoMoreInteractions(repository);
        verify(mapper).entityToResponse(mockEntity);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    @DisplayName("Should throw a ProductNotFoundException when updateProduct is call with no existing Product id")
    void test_updateProduct_failure() {
        //Arrange
        var requestDTO = new ProductRequestDTO("Name", "Description", new BigDecimal(1));
        when(repository.findById(1L)).thenReturn(Optional.empty());
        //Act, Assert
        assertThrows(ProductNotFoundException.class, () -> service.updateProduct(1L, requestDTO));
        //Verify
        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should delete a Product")
    void test_deleteProduct_success() {
        //Arrange
        var mockEntity = new Product(1L, "Name", "Description", new BigDecimal(1));
        when(repository.findById(1L)).thenReturn(Optional.of(mockEntity));
        //Act
        service.deleteProduct(1L);
        //Verify
        verify(repository).findById(1L);
        verify(repository).delete(mockEntity);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should throw a ProductNotFoundException when deleteProduct is call with no existing Product id")
    void test_deleteProduct_failure() {
        //Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());
        //Act, Assert
        assertThrows(ProductNotFoundException.class, () -> service.deleteProduct(1L));
        //Verify
        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
    }
}
