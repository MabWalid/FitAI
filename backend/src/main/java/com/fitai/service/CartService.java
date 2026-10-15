package com.fitai.service;

import com.fitai.dto.CartDto;

public interface CartService {
    CartDto getCart(String sessionId);
    CartDto addProductToCart(String sessionId, Long productId, Integer quantity);
    CartDto removeProductFromCart(String sessionId, Long productId);
    void clearCart(String sessionId);
}
