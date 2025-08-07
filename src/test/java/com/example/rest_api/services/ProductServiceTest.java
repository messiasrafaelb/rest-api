package unit.com.example.rest_api.service;

import com.example.rest_api.dtos.ProductResponseDTO;
import com.example.rest_api.mappers.ProductMapper;
import com.example.rest_api.models.Product;
import com.example.rest_api.repositories.ProductRepository;
import com.example.rest_api.services.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
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
    void test_getAllProducts_sucess() {
        var mockEntity1 = new Product(1L, "Teste", "Teste", new BigDecimal(1));
        var mockEntity2 = new Product(2L, "Teste2", "Teste2", new BigDecimal(2));
        var mockDTO1 = new ProductResponseDTO(1L,"Teste",  new BigDecimal(1));
        var mockDTO2 = new ProductResponseDTO(2L, "Teste2",  new BigDecimal(2));

        when(repository.findAll()).thenReturn(List.of(mockEntity1, mockEntity2));
        when(mapper.entityToResponse(mockEntity1)).thenReturn(mockDTO1);
        when(mapper.entityToResponse(mockEntity2)).thenReturn(mockDTO2);
        List<ProductResponseDTO> result = service.getAllProducts();

        assertThat(result).containsExactly(mockDTO1, mockDTO2);

    }

}
