package com.outfitera.service.impl;

import com.outfitera.dto.catalog.ProductRequest;
import com.outfitera.entity.Category;
import com.outfitera.entity.Product;
import com.outfitera.enums.ClothingCategory;
import com.outfitera.exception.BusinessException;
import com.outfitera.exception.ResourceNotFoundException;
import com.outfitera.mapper.CatalogMapper;
import com.outfitera.repository.CategoryRepository;
import com.outfitera.repository.ProductRepository;
import com.outfitera.repository.SizeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductServiceImpl — Tests Unitaires")
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private SizeRepository sizeRepository;
    @Spy
    private CatalogMapper catalogMapper = new CatalogMapper();

    @InjectMocks
    private ProductServiceImpl productService;

    private Category tops;

    @BeforeEach
    void setUp() {
        tops = Category.builder()
                .name("Hauts")
                .slug("hauts")
                .clothingCategory(ClothingCategory.TOP)
                .build();
        ReflectionTestUtils.setField(tops, "id", 1L);
    }

    @Test
    @DisplayName("create() — refuse un SKU déjà utilisé")
    void create_ShouldRejectDuplicateSku() {
        ProductRequest request = ProductRequest.builder()
                .name("T-shirt")
                .sku("FIT-TOP-001")
                .price(new BigDecimal("199.00"))
                .stockQuantity(10)
                .categoryId(1L)
                .build();
        when(productRepository.existsBySku("FIT-TOP-001")).thenReturn(true);

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("FIT-TOP-001");
    }

    @Test
    @DisplayName("create() — enregistre un produit rattaché à une catégorie")
    void create_ShouldPersistProduct() {
        ProductRequest request = ProductRequest.builder()
                .name("T-shirt coton blanc")
                .sku("FIT-TOP-001")
                .price(new BigDecimal("199.00"))
                .stockQuantity(10)
                .categoryId(1L)
                .imageUrls(List.of("https://example.com/tee.jpg"))
                .sizeIds(List.of())
                .build();

        when(productRepository.existsBySku("FIT-TOP-001")).thenReturn(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(tops));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product saved = inv.getArgument(0);
            ReflectionTestUtils.setField(saved, "id", 10L);
            return saved;
        });

        var response = productService.create(request);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getSku()).isEqualTo("FIT-TOP-001");
        assertThat(response.getCategory().getName()).isEqualTo("Hauts");
        assertThat(response.getImages()).hasSize(1);
    }

    @Test
    @DisplayName("findById() — 404 si le produit n'existe pas")
    void findById_ShouldThrowWhenMissing() {
        when(productRepository.findDetailedById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
