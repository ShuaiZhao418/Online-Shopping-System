package com.example.webapplication.service.serviceImpl;

import com.example.webapplication.dao.ImageRepository;
import com.example.webapplication.dao.ProductRepository;
import com.example.webapplication.entity.Image;
import com.example.webapplication.entity.Product;
import com.example.webapplication.service.ImageService;
import com.example.webapplication.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    ImageRepository imageRep;

    @Override
    public List<Image> getAllImagesOfAProduct(Integer productId) {
        List<Image> images = imageRep.findAllImagesOfAProduct(productId);
        return images;
    }

    @Override
    public List<Image> getAnImageOfAProduct(Integer productId, Integer imageId) {
        List<Image> image = imageRep.findAnImageOfAProduct(productId, imageId);
        return image;
    }

    @Override
    public Image uploadAnImage(Image image) {
        Image createdImage = imageRep.save(image);
        return createdImage;
    }

    @Override
    public void deleteAnImage(Integer imageId) {
        imageRep.deleteById(imageId);
    }
}
