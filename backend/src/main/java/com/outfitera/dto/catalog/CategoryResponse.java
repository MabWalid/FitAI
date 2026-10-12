package com.outfitera.dto.catalog;

import com.outfitera.enums.ClothingCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private ClothingCategory clothingCategory;
    private Long parentCategoryId;
}
