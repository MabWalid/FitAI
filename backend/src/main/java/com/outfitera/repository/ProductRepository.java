package com.outfitera.repository;

import com.outfitera.entity.Product;
import com.outfitera.enums.ClothingCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);

    boolean existsByCategoryId(Long categoryId);

    @EntityGraph(attributePaths = {"category", "images", "availableSizes"})
    Optional<Product> findDetailedById(Long id);

    @Query("""
            SELECT p FROM Product p
            JOIN p.category c
            WHERE (:active IS NULL OR p.active = :active)
              AND (:categoryId IS NULL OR c.id = :categoryId)
              AND (:clothingCategory IS NULL OR c.clothingCategory = :clothingCategory)
              AND (
                    :q IS NULL
                    OR LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%'))
                    OR LOWER(COALESCE(p.brand, '')) LIKE LOWER(CONCAT('%', :q, '%'))
                    OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :q, '%'))
                  )
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
            """)
    Page<Product> search(
            @Param("active") Boolean active,
            @Param("categoryId") Long categoryId,
            @Param("clothingCategory") ClothingCategory clothingCategory,
            @Param("q") String q,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);
}
