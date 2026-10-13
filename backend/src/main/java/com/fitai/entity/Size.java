package com.fitai.entity;

import com.fitai.enums.SizeType;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant une taille ou pointure (S, M, L, XL, 38, 40, 42, etc.).
 */
@Entity
@Table(name = "sizes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size extends BaseEntity {

    @Column(nullable = false, length = 20)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "size_type", nullable = false, length = 20)
    private SizeType sizeType;
}
