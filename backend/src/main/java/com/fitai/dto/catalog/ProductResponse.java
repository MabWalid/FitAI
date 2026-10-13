package com.fitai.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String sku;
    private String description;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String brand;
    private Integer stockQuantity;
    private boolean active;
    private CategoryResponse category;
    @Builder.Default
    private List<ProductImageResponse> images = new ArrayList<>();
    @Builder.Default
    private List<SizeResponse> availableSizes = new ArrayList<>();
}
