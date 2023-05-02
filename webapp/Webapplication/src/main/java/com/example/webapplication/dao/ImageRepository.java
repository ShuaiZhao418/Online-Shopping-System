package com.example.webapplication.dao;

import com.example.webapplication.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query(value = "select * from image where image.product_id = ?1", nativeQuery = true)
    List<Image> findAllImagesOfAProduct(Integer productId);

    @Query(value = "select * from image where image.product_id = ?1 and image.image_id = ?2", nativeQuery = true)
    List<Image> findAnImageOfAProduct(Integer productId, Integer imageId);
}
