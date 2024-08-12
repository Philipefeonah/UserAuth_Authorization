package com.maxiflexy.springsecurityjwt.repository;

import com.maxiflexy.springsecurityjwt.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
