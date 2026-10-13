package com.fitai.mapper;

import com.fitai.dto.catalog.CategoryResponse;
import com.fitai.dto.catalog.ProductImageResponse;
import com.fitai.dto.catalog.ProductResponse;
import com.fitai.dto.catalog.SizeResponse;
import com.fitai.entity.Category;
import com.fitai.entity.Product;
import com.fitai.entity.ProductImage;
import com.fitai.entity.Size;
import org.springframework.stereotype.Component;

@Component
public class CatalogMapper {

    public CategoryResponse toCategoryResponse(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .clothingCategory(category.getClothingCategory())
                .parentCategoryId(category.getParentCategory() != null ? category.getParentCategory().getId() : null)
                .build();
    }

    public SizeResponse toSizeResponse(Size size) {
        return SizeResponse.builder()
                .id(size.getId())
                .name(size.getName())
                .sizeType(size.getSizeType())
                .build();
    }

    public ProductImageResponse toImageResponse(ProductImage image) {
        return ProductImageResponse.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .primary(image.isPrimary())
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .description(product.getDescription())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .brand(product.getBrand())
                .stockQuantity(product.getStockQuantity())
                .active(product.isActive())
                .category(toCategoryResponse(product.getCategory()))
                .images(product.getImages().stream().map(this::toImageResponse).toList())
                .availableSizes(product.getAvailableSizes().stream().map(this::toSizeResponse).toList())
                .build();
    }
}
