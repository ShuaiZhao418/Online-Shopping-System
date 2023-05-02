package com.example.webapplication.service.serviceImpl;

import com.example.webapplication.dao.ProductRepository;
import com.example.webapplication.entity.Product;
import com.example.webapplication.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRep;

    @Override
    public Product addAProduct(Product product) {
        Product createdProduct = productRep.save(product);
        return createdProduct;
    }
 
    @Override
    public void updateProduct(Product product) {
        productRep.save(product);
    }

    @Override
    public void updateAProduct(Product product) {
        productRep.save(product);
    }

    @Override
    public void deleteAProduct(Integer productId) {
        productRep.deleteById(productId);
    }

    @Override
    public Product getAProduct(Integer productId) {
        Product product = productRep.findByProductId(productId);
        if (product == null) {
            return null;
        }
        return product;
    }

    @Override
    public Product searchProductBySku(String sku) {
        return productRep.findBySku(sku);
    }
}
