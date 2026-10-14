package com.fitai.service.impl;

import com.fitai.dto.common.PagedResponse;
import com.fitai.dto.look.LookRequest;
import com.fitai.dto.look.LookResponse;
import com.fitai.entity.Look;
import com.fitai.entity.Product;
import com.fitai.entity.User;
import com.fitai.exception.BusinessException;
import com.fitai.exception.ResourceNotFoundException;
import com.fitai.mapper.LookMapper;
import com.fitai.repository.LookRepository;
import com.fitai.repository.ProductRepository;
import com.fitai.repository.UserRepository;
import com.fitai.service.LookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LookServiceImpl implements LookService {

    private final LookRepository lookRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final LookMapper lookMapper;

    @Override
    public LookResponse createLook(Long userId, LookRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", userId));

        Look look = Look.builder()
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .user(user)
                .topProduct(resolveProduct(request.getTopProductId()))
                .bottomProduct(resolveProduct(request.getBottomProductId()))
                .shoesProduct(resolveProduct(request.getShoesProductId()))
                .accessoryProduct(resolveProduct(request.getAccessoryProductId()))
                .isPublic(request.isPublic())
                .isApproved(false) // Needs admin approval to be visible in public gallery
                .build();

        look.calculateTotalPrice();
        return lookMapper.toLookResponse(lookRepository.save(look));
    }

    @Override
    public LookResponse updateLook(Long userId, Long lookId, LookRequest request) {
        Look look = lookRepository.findDetailedById(lookId)
                .orElseThrow(() -> new ResourceNotFoundException("Look", "id", lookId));

        if (!look.getUser().getId().equals(userId)) {
            throw new BusinessException("Vous n'êtes pas autorisé à modifier ce look", HttpStatus.FORBIDDEN);
        }

        look.setTitle(request.getTitle().trim());
        look.setDescription(request.getDescription());
        look.setTopProduct(resolveProduct(request.getTopProductId()));
        look.setBottomProduct(resolveProduct(request.getBottomProductId()));
        look.setShoesProduct(resolveProduct(request.getShoesProductId()));
        look.setAccessoryProduct(resolveProduct(request.getAccessoryProductId()));
        
        // If it was approved and gets modified, it might need re-approval depending on business rules.
        // For now, let's reset approval if it goes from private to public, or just keep it simple.
        look.setPublic(request.isPublic());
        
        look.calculateTotalPrice();
        return lookMapper.toLookResponse(look);
    }

    @Override
    public void deleteLook(Long userId, Long lookId) {
        Look look = lookRepository.findById(lookId)
                .orElseThrow(() -> new ResourceNotFoundException("Look", "id", lookId));

        if (!look.getUser().getId().equals(userId)) {
            throw new BusinessException("Vous n'êtes pas autorisé à supprimer ce look", HttpStatus.FORBIDDEN);
        }

        lookRepository.delete(look);
    }

    @Override
    @Transactional(readOnly = true)
    public LookResponse getLookById(Long id) {
        Look look = lookRepository.findDetailedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Look", "id", id));
        return lookMapper.toLookResponse(look);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<LookResponse> getUserLooks(Long userId, Pageable pageable) {
        Page<Look> page = lookRepository.findByUserId(userId, pageable);
        return PagedResponse.<LookResponse>builder()
                .content(page.getContent().stream().map(lookMapper::toLookResponse).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<LookResponse> getPublicLooks(Pageable pageable) {
        Page<Look> page = lookRepository.findByIsPublicTrueAndIsApprovedTrue(pageable);
        return PagedResponse.<LookResponse>builder()
                .content(page.getContent().stream().map(lookMapper::toLookResponse).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    public LookResponse approveLook(Long lookId, boolean approved) {
        Look look = lookRepository.findDetailedById(lookId)
                .orElseThrow(() -> new ResourceNotFoundException("Look", "id", lookId));
        look.setApproved(approved);
        return lookMapper.toLookResponse(look);
    }

    private Product resolveProduct(Long productId) {
        if (productId == null) {
            return null;
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produit", "id", productId));
    }
}
