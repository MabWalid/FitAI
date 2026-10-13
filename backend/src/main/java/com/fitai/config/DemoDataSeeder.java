package com.fitai.config;

import com.fitai.entity.Category;
import com.fitai.entity.Product;
import com.fitai.entity.ProductImage;
import com.fitai.entity.Size;
import com.fitai.entity.User;
import com.fitai.enums.ClothingCategory;
import com.fitai.enums.Role;
import com.fitai.enums.SizeType;
import com.fitai.repository.CategoryRepository;
import com.fitai.repository.ProductRepository;
import com.fitai.repository.SizeRepository;
import com.fitai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Jeu de données de démo (profil dev) : admin, tailles, catégories, produits.
 * Ne s'exécute que si le catalogue est vide.
 */
@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DemoDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SizeRepository sizeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        seedAdmin();
        if (productRepository.count() > 0) {
            return;
        }
        List<Size> clothingSizes = seedSizes();
        List<Category> categories = seedCategories();
        seedProducts(categories, clothingSizes);
        log.info("FitAI — données de démo chargées (admin@fitai.dev / Admin1234!)");
    }

    private void seedAdmin() {
        if (userRepository.existsByEmail("admin@fitai.dev")) {
            return;
        }
        userRepository.save(User.builder()
                .username("admin")
                .email("admin@fitai.dev")
                .password(passwordEncoder.encode("Admin1234!"))
                .firstName("FitAI")
                .lastName("Admin")
                .role(Role.ROLE_ADMIN)
                .enabled(true)
                .build());
    }

    private List<Size> seedSizes() {
        List<Size> clothing = List.of("XS", "S", "M", "L", "XL").stream()
                .map(name -> size(name, SizeType.CLOTHING))
                .toList();
        List.of("39", "40", "41", "42", "43", "44").forEach(n -> size(n, SizeType.SHOES));
        size("Unique", SizeType.ACCESSORY);
        return clothing;
    }

    private Size size(String name, SizeType type) {
        return sizeRepository.findByNameAndSizeType(name, type)
                .orElseGet(() -> sizeRepository.save(Size.builder().name(name).sizeType(type).build()));
    }

    private List<Category> seedCategories() {
        return List.of(
                category("Hauts", "hauts", "T-shirts, chemises, vestes", ClothingCategory.TOP),
                category("Bas", "bas", "Jeans, pantalons, jupes", ClothingCategory.BOTTOM),
                category("Chaussures", "chaussures", "Baskets et derbies", ClothingCategory.SHOES),
                category("Accessoires", "accessoires", "Sacs, ceintures, lunettes", ClothingCategory.ACCESSORY)
        );
    }

    private Category category(String name, String slug, String description, ClothingCategory type) {
        return categoryRepository.findBySlug(slug)
                .orElseGet(() -> categoryRepository.save(Category.builder()
                        .name(name)
                        .slug(slug)
                        .description(description)
                        .clothingCategory(type)
                        .build()));
    }

    private void seedProducts(List<Category> categories, List<Size> clothingSizes) {
        Category tops = categories.get(0);
        Category bottoms = categories.get(1);
        Category shoes = categories.get(2);
        Category accessories = categories.get(3);
        Size unique = sizeRepository.findByNameAndSizeType("Unique", SizeType.ACCESSORY).orElseThrow();
        List<Size> shoeSizes = sizeRepository.findAll().stream()
                .filter(s -> s.getSizeType() == SizeType.SHOES)
                .toList();

        saveProduct("T-shirt coton blanc", "FIT-TOP-001", "T-shirt basique, idéal chaleur et layering.",
                "199.00", "FitAI Basics", 40, tops, clothingSizes,
                "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab");
        saveProduct("Chemise lin sable", "FIT-TOP-002", "Chemise légère pour mariage, bureau d'été ou weekend.",
                "349.00", "Atelier Sud", 20, tops, clothingSizes,
                "https://images.unsplash.com/photo-1596755094514-f87e34085b2c");
        saveProduct("Jean droit indigo", "FIT-BOT-001", "Jean coupe droite, polyvalent.",
                "399.00", "FitAI Denim", 25, bottoms, clothingSizes,
                "https://images.unsplash.com/photo-1542272604-787c3835535d");
        saveProduct("Pantalon chino beige", "FIT-BOT-002", "Chino pour tenue smart-casual jusqu'à 800 DH avec un haut.",
                "329.00", "Atelier Sud", 18, bottoms, clothingSizes,
                "https://images.unsplash.com/photo-1473966968600-fa801b869a1a");
        saveProduct("Baskets blanches", "FIT-SHOE-001", "Baskets clean pour presque toutes les tenues.",
                "449.00", "Stride", 30, shoes, shoeSizes,
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff");
        saveProduct("Derbies cuir", "FIT-SHOE-002", "Chaussures habillées pour cérémonie ou entretien.",
                "599.00", "Stride", 12, shoes, shoeSizes,
                "https://images.unsplash.com/photo-1449505278894-297fdb3edbc1");
        saveProduct("Ceinture cuir", "FIT-ACC-001", "Ceinture unie, taille unique réglable.",
                "129.00", "FitAI Basics", 50, accessories, List.of(unique),
                "https://images.unsplash.com/photo-1624222247344-550fb60583b2");
        saveProduct("Sac cabas toile", "FIT-ACC-002", "Sac quotidien, accessoire de look ville.",
                "249.00", "Atelier Sud", 15, accessories, List.of(unique),
                "https://images.unsplash.com/photo-1590874103328-eac38a941954");
    }

    private void saveProduct(
            String name,
            String sku,
            String description,
            String price,
            String brand,
            int stock,
            Category category,
            List<Size> sizes,
            String imageUrl) {
        if (productRepository.existsBySku(sku)) {
            return;
        }
        Product product = Product.builder()
                .name(name)
                .sku(sku)
                .description(description)
                .price(new BigDecimal(price))
                .brand(brand)
                .stockQuantity(stock)
                .active(true)
                .category(category)
                .availableSizes(Set.copyOf(sizes))
                .build();
        product.addImage(ProductImage.builder().imageUrl(imageUrl).primary(true).build());
        productRepository.save(product);
    }
}
