package com.fitai.service;

import com.fitai.dto.common.PagedResponse;
import com.fitai.dto.look.LookRequest;
import com.fitai.dto.look.LookResponse;
import org.springframework.data.domain.Pageable;

public interface LookService {

    LookResponse createLook(Long userId, LookRequest request);

    LookResponse updateLook(Long userId, Long lookId, LookRequest request);

    void deleteLook(Long userId, Long lookId);

    LookResponse getLookById(Long id);

    PagedResponse<LookResponse> getUserLooks(Long userId, Pageable pageable);

    PagedResponse<LookResponse> getPublicLooks(Pageable pageable);
    
    LookResponse approveLook(Long lookId, boolean approved);
}
