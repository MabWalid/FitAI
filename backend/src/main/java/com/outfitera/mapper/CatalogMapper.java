package com.outfitera.mapper;

import com.outfitera.dto.catalog.CategoryResponse;
import com.outfitera.dto.catalog.ProductImageResponse;
import com.outfitera.dto.catalog.ProductResponse;
import com.outfitera.dto.catalog.SizeResponse;
import com.outfitera.entity.Category;
import com.outfitera.entity.Product;
import com.outfitera.entity.ProductImage;
import com.outfitera.entity.Size;
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
