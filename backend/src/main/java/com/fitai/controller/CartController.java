package com.fitai.controller;

import com.fitai.dto.CartDto;
import com.fitai.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    private final CartService cartService;

    // For demo purposes, we pass the session ID in the header.
    // In a real app, this would be extracted from the JWT token or Spring Security context.
    
    @GetMapping
    public ResponseEntity<CartDto> getCart(@RequestHeader("X-Session-ID") String sessionId) {
        return ResponseEntity.ok(cartService.getCart(sessionId));
    }

    @PostMapping("/items/{productId}")
    public ResponseEntity<CartDto> addProductToCart(
            @RequestHeader("X-Session-ID") String sessionId,
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        return ResponseEntity.ok(cartService.addProductToCart(sessionId, productId, quantity));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartDto> removeProductFromCart(
            @RequestHeader("X-Session-ID") String sessionId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeProductFromCart(sessionId, productId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@RequestHeader("X-Session-ID") String sessionId) {
        cartService.clearCart(sessionId);
        return ResponseEntity.ok().build();
    }
}
