package com.fitai.dto.stylist;

import com.fitai.enums.Occasion;
import com.fitai.enums.Weather;
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
public class StylistResponse {
    private Occasion occasion;
    private Weather weather;
    private BigDecimal maxBudget;
    @Builder.Default
    private List<LookSuggestion> looks = new ArrayList<>();
    private String summary;
}
