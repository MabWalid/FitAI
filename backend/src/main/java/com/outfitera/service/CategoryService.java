package com.outfitera.service;

import com.outfitera.dto.catalog.CategoryRequest;
import com.outfitera.dto.catalog.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> findAll();

    CategoryResponse findById(Long id);

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);
}
