package com.example.webapplication.service;

import com.example.webapplication.entity.Image;
import com.example.webapplication.entity.Product;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {
    // get all images of a product
    List<Image> getAllImagesOfAProduct(Integer productId);
    // get an image of a product
    List<Image> getAnImageOfAProduct(Integer productId, Integer imageId);
    // upload an image to a product
    Image uploadAnImage(Image image);
    // delete an image
    void deleteAnImage(Integer imageId);

}
