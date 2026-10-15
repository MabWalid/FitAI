package com.fitai.service.impl;

import com.fitai.dto.CartDto;
import com.fitai.dto.CartItemDto;
import com.fitai.dto.catalog.CategoryResponse;
import com.fitai.dto.catalog.ProductResponse;
import com.fitai.dto.catalog.ProductImageResponse;
import com.fitai.entity.Cart;
import com.fitai.entity.CartItem;
import com.fitai.entity.Product;
import com.fitai.repository.CartRepository;
import com.fitai.repository.ProductRepository;
import com.fitai.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public CartDto getCart(String sessionId) {
        Cart cart = cartRepository.findBySessionId(sessionId)
                .orElseGet(() -> createNewCart(sessionId));
        return mapToDto(cart);
    }

    @Override
    @Transactional
    public CartDto addProductToCart(String sessionId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findBySessionId(sessionId)
                .orElseGet(() -> createNewCart(sessionId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cart.addItem(newItem);
        }

        cartRepository.save(cart);
        return mapToDto(cart);
    }

    @Override
    @Transactional
    public CartDto removeProductFromCart(String sessionId, Long productId) {
        Cart cart = cartRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cartRepository.save(cart);
        
        return mapToDto(cart);
    }

    @Override
    @Transactional
    public void clearCart(String sessionId) {
        cartRepository.findBySessionId(sessionId).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepository.save(cart);
        });
    }

    private Cart createNewCart(String sessionId) {
        Cart cart = Cart.builder()
                .sessionId(sessionId)
                .build();
        return cartRepository.save(cart);
    }

    private CartDto mapToDto(Cart cart) {
        List<CartItemDto> itemDtos = cart.getItems().stream().map(item -> 
            CartItemDto.builder()
                .id(item.getId())
                .quantity(item.getQuantity())
                .product(ProductResponse.builder()
                        .id(item.getProduct().getId())
                        .name(item.getProduct().getName())
                        .description(item.getProduct().getDescription())
                        .price(item.getProduct().getPrice())
                        .images(item.getProduct().getImages().stream().map(img -> 
                            ProductImageResponse.builder()
                                .id(img.getId())
                                .imageUrl(img.getImageUrl())
                                .primary(img.isPrimary())
                                .build()
                        ).collect(Collectors.toList()))
                        .category(CategoryResponse.builder()
                                .id(item.getProduct().getCategory().getId())
                                .name(item.getProduct().getCategory().getName())
                                .build())
                        .build())
                .build()
        ).collect(Collectors.toList());

        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartDto.builder()
                .id(cart.getId())
                .sessionId(cart.getSessionId())
                .items(itemDtos)
                .totalPrice(total)
                .build();
    }
}
