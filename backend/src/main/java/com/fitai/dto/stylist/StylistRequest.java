package com.fitai.dto.stylist;

import com.fitai.enums.Occasion;
import com.fitai.enums.Weather;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StylistRequest {

    @NotNull(message = "L'occasion est obligatoire (CASUAL, OFFICE, WEDDING, SPORT)")
    private Occasion occasion;

    @NotNull(message = "Le budget maximum est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le budget doit être supérieur à 0")
    private BigDecimal maxBudget;

    @NotNull(message = "La météo est obligatoire (HOT, MILD, COLD)")
    private Weather weather;

    private String notes;
}
