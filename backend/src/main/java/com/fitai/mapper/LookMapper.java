package com.fitai.mapper;

import com.fitai.dto.look.LookResponse;
import com.fitai.entity.Look;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LookMapper {

    private final CatalogMapper catalogMapper;

    public LookResponse toLookResponse(Look look) {
        if (look == null) {
            return null;
        }
        return LookResponse.builder()
                .id(look.getId())
                .title(look.getTitle())
                .description(look.getDescription())
                .userId(look.getUser() != null ? look.getUser().getId() : null)
                .username(look.getUser() != null ? look.getUser().getUsername() : null)
                .topProduct(catalogMapper.toProductResponse(look.getTopProduct()))
                .bottomProduct(catalogMapper.toProductResponse(look.getBottomProduct()))
                .shoesProduct(catalogMapper.toProductResponse(look.getShoesProduct()))
                .accessoryProduct(catalogMapper.toProductResponse(look.getAccessoryProduct()))
                .totalPrice(look.getTotalPrice())
                .isPublic(look.isPublic())
                .isApproved(look.isApproved())
                .createdAt(look.getCreatedAt())
                .build();
    }
}
