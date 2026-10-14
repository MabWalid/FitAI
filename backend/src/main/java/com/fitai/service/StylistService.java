package com.fitai.service;

import com.fitai.dto.stylist.StylistRequest;
import com.fitai.dto.stylist.StylistResponse;

public interface StylistService {
    
    /**
     * Génère des suggestions de tenues en fonction de l'occasion, du budget et de la météo.
     */
    StylistResponse generateLooks(StylistRequest request);
}
