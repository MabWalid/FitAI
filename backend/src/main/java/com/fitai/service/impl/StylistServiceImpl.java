package com.fitai.service.impl;

import com.fitai.dto.catalog.ProductResponse;
import com.fitai.dto.stylist.LookSuggestion;
import com.fitai.dto.stylist.StylistRequest;
import com.fitai.dto.stylist.StylistResponse;
import com.fitai.entity.Product;
import com.fitai.enums.ClothingCategory;
import com.fitai.mapper.CatalogMapper;
import com.fitai.repository.ProductRepository;
import com.fitai.service.StylistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class StylistServiceImpl implements StylistService {

    private final ProductRepository productRepository;
    private final CatalogMapper catalogMapper;
    private final Random random = new Random();

    @Override
    @Transactional(readOnly = true)
    public StylistResponse generateLooks(StylistRequest request) {
        log.info("Génération de looks pour occasion={}, météo={}, budget={}", 
                request.getOccasion(), request.getWeather(), request.getMaxBudget());

        // 1. Récupérer tous les produits actifs
        List<Product> allProducts = productRepository.findAll().stream()
                .filter(Product::isActive)
                .toList();

        // 2. Séparer par catégorie
        List<Product> tops = filterByCategory(allProducts, ClothingCategory.TOP);
        List<Product> bottoms = filterByCategory(allProducts, ClothingCategory.BOTTOM);
        List<Product> shoes = filterByCategory(allProducts, ClothingCategory.SHOES);
        List<Product> accessories = filterByCategory(allProducts, ClothingCategory.ACCESSORY);

        List<LookSuggestion> suggestions = new ArrayList<>();

        // 3. Générer jusqu'à 3 propositions (algorithme heuristique simple pour la démo)
        for (int i = 0; i < 10 && suggestions.size() < 3; i++) {
            Product top = getRandom(tops);
            Product bottom = getRandom(bottoms);
            Product shoe = getRandom(shoes);
            Product accessory = getRandom(accessories);

            if (top != null && bottom != null && shoe != null) {
                BigDecimal total = getPrice(top).add(getPrice(bottom)).add(getPrice(shoe));
                if (accessory != null) {
                    total = total.add(getPrice(accessory));
                }

                // Vérifier le budget
                if (total.compareTo(request.getMaxBudget()) <= 0) {
                    // S'assurer qu'on n'a pas déjà cette combinaison
                    boolean exists = suggestions.stream().anyMatch(s -> 
                        s.getTop().getId().equals(top.getId()) && 
                        s.getBottom().getId().equals(bottom.getId())
                    );
                    
                    if (!exists) {
                        suggestions.add(buildSuggestion(top, bottom, shoe, accessory, total, request));
                    }
                }
            }
        }

        String summary = suggestions.isEmpty() 
            ? "Désolé, nous n'avons pas trouvé de tenue complète correspondant à ce budget."
            : String.format("Voici %d suggestion(s) parfaite(s) pour un événement %s par temps %s.", 
                suggestions.size(), request.getOccasion().name().toLowerCase(), request.getWeather().name().toLowerCase());

        return StylistResponse.builder()
                .occasion(request.getOccasion())
                .weather(request.getWeather())
                .maxBudget(request.getMaxBudget())
                .looks(suggestions)
                .summary(summary)
                .build();
    }

    private List<Product> filterByCategory(List<Product> products, ClothingCategory category) {
        return products.stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getClothingCategory() == category)
                .toList();
    }

    private Product getRandom(List<Product> products) {
        if (products == null || products.isEmpty()) return null;
        return products.get(random.nextInt(products.size()));
    }

    private BigDecimal getPrice(Product p) {
        if (p == null) return BigDecimal.ZERO;
        return p.getDiscountPrice() != null ? p.getDiscountPrice() : p.getPrice();
    }

    private LookSuggestion buildSuggestion(Product top, Product bottom, Product shoe, Product accessory, BigDecimal total, StylistRequest request) {
        ProductResponse topDto = catalogMapper.toProductResponse(top);
        ProductResponse bottomDto = catalogMapper.toProductResponse(bottom);
        ProductResponse shoeDto = catalogMapper.toProductResponse(shoe);
        ProductResponse accDto = accessory != null ? catalogMapper.toProductResponse(accessory) : null;

        List<ProductResponse> items = new ArrayList<>();
        items.add(topDto);
        items.add(bottomDto);
        items.add(shoeDto);
        if (accDto != null) items.add(accDto);

        String title = "Look " + request.getOccasion().name() + " Élégant";
        String reason = "Cette combinaison de " + top.getName() + " et " + bottom.getName() + " est idéale pour l'occasion, tout en respectant votre budget de " + request.getMaxBudget() + " DH.";

        return LookSuggestion.builder()
                .title(title)
                .reason(reason)
                .totalPrice(total)
                .top(topDto)
                .bottom(bottomDto)
                .shoes(shoeDto)
                .accessory(accDto)
                .items(items)
                .build();
    }
}
