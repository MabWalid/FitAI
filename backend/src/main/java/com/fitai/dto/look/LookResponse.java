package com.fitai.dto.look;

import com.fitai.dto.catalog.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LookResponse {
    private Long id;
    private String title;
    private String description;
    
    private Long userId;
    private String username;

    private ProductResponse topProduct;
    private ProductResponse bottomProduct;
    private ProductResponse shoesProduct;
    private ProductResponse accessoryProduct;

    private BigDecimal totalPrice;
    private boolean isPublic;
    private boolean isApproved;
    
    private LocalDateTime createdAt;
}
