package com.fitai.controller;

import com.fitai.dto.catalog.ProductRequest;
import com.fitai.dto.catalog.ProductResponse;
import com.fitai.dto.common.PagedResponse;
import com.fitai.enums.ClothingCategory;
import com.fitai.service.ProductService;
import com.fitai.util.ApiResponse;
import com.fitai.util.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Catalogue FitAI — produits, filtres et pagination")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Rechercher des produits (public, actifs par défaut)")
    public ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> search(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) ClothingCategory clothingCategory,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Boolean activeFilter = active != null ? active : Boolean.TRUE;
        PagedResponse<ProductResponse> result = productService.search(
                activeFilter, categoryId, clothingCategory, q, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(ApiResponse.success("Produits récupérés", result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'un produit")
    public ResponseEntity<ApiResponse<ProductResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Produit récupéré", productService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Créer un produit (admin)")
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Produit créé", productService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Modifier un produit (admin)")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Produit mis à jour", productService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Archiver un produit (admin, active=false)")
    public ResponseEntity<ApiResponse<Void>> archive(@PathVariable Long id) {
        productService.archive(id);
        return ResponseEntity.ok(ApiResponse.success("Produit archivé"));
    }
}
