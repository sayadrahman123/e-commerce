package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
     List<Product> findByNameContainingIgnoreCase(String name);
     List<Product> findByCategoryIgnoreCase(String category);
     List<Product> findByBrandIgnoreCase(String brand);
     List<Product> findByPriceBetween(double min, double max);


}
