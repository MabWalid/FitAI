package com.fitai.service.impl;

import com.fitai.dto.catalog.ProductRequest;
import com.fitai.dto.catalog.ProductResponse;
import com.fitai.dto.common.PagedResponse;
import com.fitai.entity.Category;
import com.fitai.entity.Product;
import com.fitai.entity.ProductImage;
import com.fitai.entity.Size;
import com.fitai.enums.ClothingCategory;
import com.fitai.exception.BusinessException;
import com.fitai.exception.ResourceNotFoundException;
import com.fitai.mapper.CatalogMapper;
import com.fitai.repository.CategoryRepository;
import com.fitai.repository.ProductRepository;
import com.fitai.repository.SizeRepository;
import com.fitai.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SizeRepository sizeRepository;
    private final CatalogMapper catalogMapper;

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProductResponse> search(
            Boolean active,
            Long categoryId,
            ClothingCategory clothingCategory,
            String q,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable) {

        String pattern = StringUtils.hasText(q)
                ? "%" + q.trim().toLowerCase(Locale.ROOT) + "%"
                : "%";
        Page<Product> page = productRepository.search(
                active, categoryId, clothingCategory, pattern, minPrice, maxPrice, pageable);

        return PagedResponse.<ProductResponse>builder()
                .content(page.getContent().stream().map(catalogMapper::toProductResponse).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = productRepository.findDetailedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit", "id", id));
        return catalogMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        String sku = request.getSku().trim();
        if (productRepository.existsBySku(sku)) {
            throw new BusinessException("Un produit existe déjà avec le SKU : " + sku, HttpStatus.CONFLICT);
        }
        validatePrices(request);

        Product product = Product.builder()
                .name(request.getName().trim())
                .sku(sku)
                .description(request.getDescription())
                .price(request.getPrice())
                .discountPrice(request.getDiscountPrice())
                .brand(request.getBrand())
                .stockQuantity(request.getStockQuantity())
                .active(request.isActive())
                .category(getCategory(request.getCategoryId()))
                .availableSizes(resolveSizes(request.getSizeIds()))
                .build();
        applyImages(product, request.getImageUrls());
        return catalogMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findDetailedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit", "id", id));
        String sku = request.getSku().trim();
        if (productRepository.existsBySkuAndIdNot(sku, id)) {
            throw new BusinessException("Un produit existe déjà avec le SKU : " + sku, HttpStatus.CONFLICT);
        }
        validatePrices(request);

        product.setName(request.getName().trim());
        product.setSku(sku);
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setBrand(request.getBrand());
        product.setStockQuantity(request.getStockQuantity());
        product.setActive(request.isActive());
        product.setCategory(getCategory(request.getCategoryId()));
        product.getAvailableSizes().clear();
        product.getAvailableSizes().addAll(resolveSizes(request.getSizeIds()));
        product.getImages().clear();
        applyImages(product, request.getImageUrls());
        return catalogMapper.toProductResponse(product);
    }

    @Override
    public void archive(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit", "id", id));
        product.setActive(false);
    }

    private void validatePrices(ProductRequest request) {
        if (request.getDiscountPrice() != null
                && request.getDiscountPrice().compareTo(request.getPrice()) >= 0) {
            throw new BusinessException("Le prix promo doit être inférieur au prix normal");
        }
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie", "id", categoryId));
    }

    private Set<Size> resolveSizes(List<Long> sizeIds) {
        if (sizeIds == null || sizeIds.isEmpty()) {
            return new HashSet<>();
        }
        List<Size> sizes = sizeRepository.findAllById(sizeIds);
        if (sizes.size() != sizeIds.stream().distinct().count()) {
            throw new ResourceNotFoundException("Taille", "id", sizeIds);
        }
        return new HashSet<>(sizes);
    }

    private void applyImages(Product product, List<String> imageUrls) {
        if (imageUrls == null) {
            return;
        }
        for (int i = 0; i < imageUrls.size(); i++) {
            String url = imageUrls.get(i);
            if (!StringUtils.hasText(url)) {
                continue;
            }
            product.addImage(ProductImage.builder()
                    .imageUrl(url.trim())
                    .primary(i == 0)
                    .build());
        }
    }
}
