package com.fitai.controller;

import com.fitai.dto.stylist.StylistRequest;
import com.fitai.dto.stylist.StylistResponse;
import com.fitai.service.StylistService;
import com.fitai.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/stylist")
@RequiredArgsConstructor
@Tag(name = "AI Stylist", description = "Générateur de tenues intelligent basé sur le catalogue")
public class StylistController {

    private final StylistService stylistService;

    @PostMapping
    @Operation(summary = "Demander une suggestion de tenue", 
               description = "L'IA analyse le catalogue et propose des looks complets respectant l'occasion, la météo et le budget.")
    public ResponseEntity<ApiResponse<StylistResponse>> generateLooks(@Valid @RequestBody StylistRequest request) {
        StylistResponse response = stylistService.generateLooks(request);
        return ResponseEntity.ok(ApiResponse.success("Suggestions générées avec succès", response));
    }
}
