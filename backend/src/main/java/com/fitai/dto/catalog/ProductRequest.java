package com.fitai.dto.catalog;

import jakarta.validation.constraints.*;
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
public class ProductRequest {

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "Le SKU est obligatoire")
    @Size(max = 100)
    private String sku;

    @Size(max = 5000)
    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    private BigDecimal price;

    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix promo doit être supérieur à 0")
    private BigDecimal discountPrice;

    @Size(max = 100)
    private String brand;

    @NotNull
    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    private Integer stockQuantity;

    @Builder.Default
    private boolean active = true;

    @NotNull(message = "La catégorie est obligatoire")
    private Long categoryId;

    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    @Builder.Default
    private List<Long> sizeIds = new ArrayList<>();
}
