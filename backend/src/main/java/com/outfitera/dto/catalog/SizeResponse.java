package com.outfitera.dto.catalog;

import com.outfitera.enums.SizeType;
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
