package com.fitai.dto.catalog;

import com.fitai.enums.SizeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SizeResponse {
    private Long id;
    private String name;
    private SizeType sizeType;
}
