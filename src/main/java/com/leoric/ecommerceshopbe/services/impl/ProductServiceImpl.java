package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Category;
import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.repositories.CategoryRepository;
import com.leoric.ecommerceshopbe.repositories.ProductRepository;
import com.leoric.ecommerceshopbe.requests.dto.CreateProductReqDto;
import com.leoric.ecommerceshopbe.services.interfaces.ProductService;
import com.leoric.ecommerceshopbe.utils.GlobalUtil;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final GlobalUtil globalUtil;

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
    public Product createProduct(CreateProductReqDto productReq, Seller seller) {
        Optional<Category> categoryOpt1 = categoryRepository.findByCategoryId(productReq.getCategory());
        Category category1;
        if (categoryOpt1.isEmpty()) {
            Category category = new Category();
            category.setCategoryId(productReq.getCategory());
            category.setLevel(1);
            category1 = categoryRepository.save(category);
        } else {
            category1 = categoryOpt1.get();
        }
        Optional<Category> categoryOpt2 = categoryRepository.findByCategoryId(productReq.getCategory2());
        Category category2;
        if (categoryOpt2.isEmpty()) {
            Category category = new Category();
            category.setCategoryId(productReq.getCategory2());
            category.setLevel(2);
            category.setParentCategory(category1);
            category2 = category;
            categoryRepository.save(category);
        } else {
            category2 = categoryOpt2.get();
        }
        Product createdProduct;
        Optional<Category> categoryOpt3 = categoryRepository.findByCategoryId(productReq.getCategory3());
        Category category3;
        if (categoryOpt3.isEmpty()) {
            Category category = new Category();
            category.setCategoryId(productReq.getCategory3());
            category.setLevel(3);
            category.setParentCategory(category2);
            category3 = categoryRepository.save(category);

            createdProduct = getCreatedProduct(productReq, seller, category3);
        } else {
            createdProduct = getCreatedProduct(productReq, seller, categoryOpt3.get());
        }
        return productRepository.save(createdProduct);
    }

    private Product getCreatedProduct(CreateProductReqDto productReq, Seller seller, Category category3) {
        int discountPercentage = calculateDiscountPercentage(productReq.getMaxPrice(), productReq.getSellingPrice());
        Product createdProduct = new Product();
        createdProduct.setSeller(seller);
        createdProduct.setCategory(category3);
        createdProduct.setDescription(productReq.getDescription());
        createdProduct.setTitle(productReq.getTitle());
        createdProduct.setColor(productReq.getColor());
        createdProduct.setSellingPrice(productReq.getSellingPrice());
        createdProduct.setImage(productReq.getImages());
        createdProduct.setMaxPrice(productReq.getMaxPrice());
        createdProduct.setSize(productReq.getSizes());
        createdProduct.setDiscountPercentage(discountPercentage);
        return createdProduct;
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
    @Transactional
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
    public List<Product> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    @Override
    public List<Product> getProductsOfCurrentUser(Authentication authentication) {
        Seller seller = globalUtil.getPrincipalAsSeller(authentication);
        return getProductsBySellerId(seller.getId());
    }

    private int calculateDiscountPercentage(double maxPrice, double sellingPrice) {
        if (sellingPrice > maxPrice || sellingPrice < 0 || maxPrice < 0) {
            throw new RuntimeException("Invalid selling price");
        }
        double discount = maxPrice - sellingPrice;
        double percentage = (discount / maxPrice) * 100;
        return (int) percentage;
    }
}