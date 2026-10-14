package com.fitai.controller;

import com.fitai.dto.common.PagedResponse;
import com.fitai.dto.look.LookRequest;
import com.fitai.dto.look.LookResponse;
import com.fitai.security.UserPrincipal;
import com.fitai.service.LookService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/looks")
@RequiredArgsConstructor
@Tag(name = "Looks", description = "Essayage virtuel — création et gestion des tenues")
public class LookController {

    private final LookService lookService;

    @GetMapping("/public")
    @Operation(summary = "Lister les looks publics et approuvés")
    public ResponseEntity<ApiResponse<PagedResponse<LookResponse>>> getPublicLooks(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(ApiResponse.success("Looks publics récupérés", lookService.getPublicLooks(pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'un look")
    public ResponseEntity<ApiResponse<LookResponse>> getLookById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Look récupéré", lookService.getLookById(id)));
    }

    @GetMapping("/my-looks")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Lister mes looks (utilisateur connecté)")
    public ResponseEntity<ApiResponse<PagedResponse<LookResponse>>> getMyLooks(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(ApiResponse.success("Vos looks", lookService.getUserLooks(currentUser.getId(), pageable)));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Créer un nouveau look (essayage virtuel)")
    public ResponseEntity<ApiResponse<LookResponse>> createLook(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody LookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Look créé avec succès", lookService.createLook(currentUser.getId(), request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Modifier un de mes looks")
    public ResponseEntity<ApiResponse<LookResponse>> updateLook(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody LookRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Look mis à jour", lookService.updateLook(currentUser.getId(), id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Supprimer un de mes looks")
    public ResponseEntity<ApiResponse<Void>> deleteLook(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        lookService.deleteLook(currentUser.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Look supprimé"));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Approuver ou rejeter un look public (Admin)")
    public ResponseEntity<ApiResponse<LookResponse>> approveLook(
            @PathVariable Long id,
            @RequestParam boolean approved) {
        return ResponseEntity.ok(ApiResponse.success("Statut d'approbation mis à jour", lookService.approveLook(id, approved)));
    }
}
