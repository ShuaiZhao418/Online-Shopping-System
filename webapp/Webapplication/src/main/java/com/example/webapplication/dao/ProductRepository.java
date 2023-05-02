package com.example.webapplication.dao;

import com.example.webapplication.entity.Product;
import com.example.webapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findBySku(String sku);
    @Query(value = "select * from product where product.id = ?1", nativeQuery = true)
    Product findByProductId(Integer userId);
}
