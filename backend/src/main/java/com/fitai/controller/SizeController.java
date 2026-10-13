package com.fitai.controller;

import com.fitai.dto.catalog.SizeResponse;
import com.fitai.mapper.CatalogMapper;
import com.fitai.repository.SizeRepository;
import com.fitai.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sizes")
@RequiredArgsConstructor
@Tag(name = "Sizes", description = "Tailles et pointures du catalogue FitAI")
public class SizeController {

    private final SizeRepository sizeRepository;
    private final CatalogMapper catalogMapper;

    @GetMapping
    @Operation(summary = "Lister les tailles")
    public ResponseEntity<ApiResponse<List<SizeResponse>>> findAll() {
        List<SizeResponse> sizes = sizeRepository.findAllByOrderBySizeTypeAscNameAsc().stream()
                .map(catalogMapper::toSizeResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Tailles récupérées", sizes));
    }
}
