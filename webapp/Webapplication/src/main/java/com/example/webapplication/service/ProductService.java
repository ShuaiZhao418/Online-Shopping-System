package com.example.webapplication.service;

import com.example.webapplication.entity.Product;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    // Add a new product
    Product addAProduct(Product product);
    // (put) update product
    void updateProduct(Product product);
    // (patch) update product
    void updateAProduct(Product product);
    // delete the product
    void deleteAProduct(Integer productId);
    // get a product
    Product getAProduct(Integer productId);

    Product searchProductBySku(String sku);
}
