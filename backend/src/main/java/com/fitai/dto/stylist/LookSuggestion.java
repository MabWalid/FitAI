package com.fitai.dto.stylist;

import com.fitai.dto.catalog.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LookSuggestion {
    private String title;
    private String reason;
    private BigDecimal totalPrice;
    private ProductResponse top;
    private ProductResponse bottom;
    private ProductResponse shoes;
    private ProductResponse accessory;
    @Builder.Default
    private List<ProductResponse> items = new ArrayList<>();
}
