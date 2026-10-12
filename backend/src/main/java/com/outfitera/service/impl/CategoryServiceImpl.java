package com.outfitera.service.impl;

import com.outfitera.dto.catalog.CategoryRequest;
import com.outfitera.dto.catalog.CategoryResponse;
import com.outfitera.entity.Category;
import com.outfitera.exception.BusinessException;
import com.outfitera.exception.ResourceNotFoundException;
import com.outfitera.mapper.CatalogMapper;
import com.outfitera.repository.CategoryRepository;
import com.outfitera.repository.ProductRepository;
import com.outfitera.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CatalogMapper catalogMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(catalogMapper::toCategoryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return catalogMapper.toCategoryResponse(getCategory(id));
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName().trim())
                .slug(uniqueSlug(request.getName(), null))
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .clothingCategory(request.getClothingCategory())
                .parentCategory(resolveParent(request.getParentCategoryId()))
                .build();
        return catalogMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getCategory(id);
        if (request.getParentCategoryId() != null && request.getParentCategoryId().equals(id)) {
            throw new BusinessException("Une catégorie ne peut pas être son propre parent");
        }
        category.setName(request.getName().trim());
        category.setSlug(uniqueSlug(request.getName(), id));
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setClothingCategory(request.getClothingCategory());
        category.setParentCategory(resolveParent(request.getParentCategoryId()));
        return catalogMapper.toCategoryResponse(category);
    }

    @Override
    public void delete(Long id) {
        Category category = getCategory(id);
        if (productRepository.existsByCategoryId(id)) {
            throw new BusinessException(
                    "Impossible de supprimer cette catégorie : des produits y sont encore liés",
                    HttpStatus.CONFLICT);
        }
        categoryRepository.delete(category);
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie", "id", id));
    }

    private Category resolveParent(Long parentId) {
        if (parentId == null) {
            return null;
        }
        return getCategory(parentId);
    }

    private String uniqueSlug(String name, Long currentId) {
        String base = slugify(name);
        String slug = base;
        int i = 2;
        while (true) {
            var existing = categoryRepository.findBySlug(slug);
            if (existing.isEmpty() || (currentId != null && existing.get().getId().equals(currentId))) {
                return slug;
            }
            slug = base + "-" + i++;
        }
    }

    private String slugify(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        return normalized.isBlank() ? "categorie" : normalized;
    }
}
