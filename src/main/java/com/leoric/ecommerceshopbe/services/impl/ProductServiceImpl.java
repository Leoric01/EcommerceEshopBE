package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Category;
import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.repositories.CategoryRepository;
import com.leoric.ecommerceshopbe.repositories.ProductRepository;
import com.leoric.ecommerceshopbe.requests.CreateProductReq;
import com.leoric.ecommerceshopbe.services.interfaces.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    @Override
    public Product save(Product entity) {
        return productRepository.save(entity);
    }

    @Override
    @Transactional
    public Product createProduct(CreateProductReq productReq, Seller seller) {
        Category category1 = categoryRepository.findByCategoryId(productReq.getCategory());
        if (category1 == null) {
            Category category = new Category();
            category.setCategoryId(productReq.getCategory());
            category.setLevel(1);
            category1 = categoryRepository.save(category);
        }
        Category category2 = categoryRepository.findByCategoryId(productReq.getCategory2());
        if (category2 == null) {
            Category category = new Category();
            category.setCategoryId(productReq.getCategory2());
            category.setLevel(2);
            category2 = categoryRepository.save(category);
        }
        Category category3 = categoryRepository.findByCategoryId(productReq.getCategory3());
        if (category3 == null) {
            Category category = new Category();
            category.setCategoryId(productReq.getCategory3());
            category.setLevel(3);
            category3 = categoryRepository.save(category);
        }
        int discountPercentage = calculateDiscountPercentage(productReq.getMrpPrice(), productReq.getSellingPrice());
        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(productReq.getDescription());
        product.setTitle(productReq.getTitle());
        product.setColor(productReq.getColor());
        product.setSellingPrice(productReq.getSellingPrice());
        product.setImage(product.getImage());
        product.setMrpPrice(product.getMrpPrice());
        product.setSizes(productReq.getSizes());

        product.setDiscountPercentage(discountPercentage);

        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product updateProduct(Long productId, Product productReq) {
        findProductById(productId);
        productReq.setId(productId);
        return productRepository.save(productReq);
    }

    @Override
    public List<Product> searchProduct(String productName) {
        return List.of();
    }

    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchProducts(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String size,
                                        Integer minPrice, Integer maxPrice, Integer minDiscount, String sort,
                                        String stock, Integer pageNumber) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (category != null) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }
            if (colors != null && !colors.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("color"), colors.toLowerCase()));
            }
            if (size != null && !size.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("size"), size.toLowerCase()));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }
            if (minDiscount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"), minDiscount));
            }
            if (stock != null && !stock.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable;
        if (sort != null && !sort.isBlank()) {
            pageable = switch (sort) {
                case "price_low" -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").ascending());
                case "price_high" -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.unsorted());
            };
        } else {
            pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
        }
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    private int calculateDiscountPercentage(double mrpPrice, double sellingPrice) {
        if (sellingPrice > mrpPrice || sellingPrice < 0 || mrpPrice < 0) {
            throw new RuntimeException("Invalid selling price");
        }
        double discount = mrpPrice - sellingPrice;
        double percentage = (discount / mrpPrice) * 100;
        return (int) percentage;
    }
}
