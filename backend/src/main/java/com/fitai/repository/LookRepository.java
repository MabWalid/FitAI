package com.fitai.repository;

import com.fitai.entity.Look;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LookRepository extends JpaRepository<Look, Long> {

    @EntityGraph(attributePaths = {"user", "topProduct", "bottomProduct", "shoesProduct", "accessoryProduct"})
    Page<Look> findByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "topProduct", "bottomProduct", "shoesProduct", "accessoryProduct"})
    Page<Look> findByIsPublicTrueAndIsApprovedTrue(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "topProduct", "bottomProduct", "shoesProduct", "accessoryProduct"})
    Optional<Look> findDetailedById(Long id);
}
