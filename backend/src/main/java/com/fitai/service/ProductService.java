package com.fitai.service;

import com.fitai.dto.catalog.ProductRequest;
import com.fitai.dto.catalog.ProductResponse;
import com.fitai.dto.common.PagedResponse;
import com.fitai.enums.ClothingCategory;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {

    PagedResponse<ProductResponse> search(
            Boolean active,
            Long categoryId,
            ClothingCategory clothingCategory,
            String q,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable);

    ProductResponse findById(Long id);

    ProductResponse create(ProductRequest request);

    ProductResponse update(Long id, ProductRequest request);

    void archive(Long id);
}
