package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.requests.CreateProductReq;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product createProduct(CreateProductReq productReq, Seller seller);

    void deleteById(Long id);

    Product updateProduct(Long productId, Product productReq);

    List<Product> searchProduct(String productName);

    List<Product> searchProducts(String query);

    Page<Product> getAllProducts(String category,
                                 String brand,
                                 String colors,
                                 String sizes,
                                 Integer minPrice,
                                 Integer maxPrice,
                                 Integer minDiscount,
                                 String sort,
                                 String stock,
                                 Integer pageNumber);

    List<Product> getProductBySellerId(Long sellerId);

    Product findProductById(Long id);

    Product save(Product entity);
    List<Product> findAll();

}
