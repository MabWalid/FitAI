package com.fitai.dto.catalog;

import com.fitai.enums.ClothingCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Le nom de la catégorie est obligatoire")
    @Size(max = 100)
    private String name;

    @Size(max = 2000)
    private String description;

    private String imageUrl;

    @NotNull(message = "Le type vestimentaire (TOP, BOTTOM, SHOES, ACCESSORY) est obligatoire")
    private ClothingCategory clothingCategory;

    private Long parentCategoryId;
}
