package com.fitai.dto.look;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LookRequest {

    @NotBlank(message = "Le titre du look est obligatoire")
    @Size(max = 150)
    private String title;

    @Size(max = 2000)
    private String description;

    private Long topProductId;
    private Long bottomProductId;
    private Long shoesProductId;
    private Long accessoryProductId;

    @Builder.Default
    private boolean isPublic = false;
}
