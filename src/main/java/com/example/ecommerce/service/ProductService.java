package com.example.ecommerce.service;

import com.example.ecommerce.exception.productException.ProductNotFoundException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.criteria.Predicate;




@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    public ResponseEntity<?> getProductById(Long id) {
        return productRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No Product Found with ID: " + id));
    }

    public ResponseEntity<?> addProduct(Product product) {
        return ResponseEntity.ok(productRepository.save(product));
    }

    public ResponseEntity<?> addProducts(List<Product> products) {
        return ResponseEntity.ok(productRepository.saveAll(products));
    }

    public ResponseEntity<?> updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setStock(product.getStock());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setSku(product.getSku());

        productRepository.save(existingProduct);
        return ResponseEntity.ok(Map.of(
                "message", "Product updated successfully",
                "product", existingProduct));

    }

    public ResponseEntity<?> deleteProduct(Long id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.ok().body("Deleted Product with ID: " + id);
        }
        return ResponseEntity.badRequest().body("No Product Found with ID: " + id);
    }

    @Transactional
    public void updateProductStock(Long id, int stock) {
        if (productRepository.findById(id).isPresent()) {
            Product product = productRepository.findById(id).get();
            product.setStock(stock);
            ResponseEntity.ok(productRepository.save(product));
            return;
        }

        ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Product Found with ID: " + id);
    }

    public ResponseEntity<?> searchProducts(String name, String category, String brand, Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null)
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            if (category != null)
                predicates.add(cb.equal(cb.lower(root.get("category")), category.toLowerCase()));
            if (brand != null)
                predicates.add(cb.equal(cb.lower(root.get("brand")), brand.toLowerCase()));
            if (minPrice != null && maxPrice != null)
                predicates.add(cb.between(root.get("price"), minPrice, maxPrice));



            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Product> products = productRepository.findAll(spec, pageable);

        return ResponseEntity.ok(products);
    }


}
