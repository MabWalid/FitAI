package com.outfitera.service;

import com.outfitera.dto.catalog.ProductRequest;
import com.outfitera.dto.catalog.ProductResponse;
import com.outfitera.dto.common.PagedResponse;
import com.outfitera.enums.ClothingCategory;
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
