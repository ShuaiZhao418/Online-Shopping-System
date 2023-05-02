package com.example.webapplication.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.webapplication.entity.Image;
import com.example.webapplication.entity.Product;
import com.example.webapplication.entity.User;
import com.example.webapplication.service.ImageService;
import com.example.webapplication.service.ProductService;
import com.example.webapplication.service.UserService;
import com.example.webapplication.utils.AWSConfig;
import com.example.webapplication.utils.Decode;
import com.example.webapplication.utils.Hash;
import com.example.webapplication.utils.RandomStringGenerator;
import com.timgroup.statsd.StatsDClient;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/product")
public class ImageController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    ImageService imageService;

    @Autowired
    private StatsDClient statsDClient;

    private static final String AUTH_HEADER_PARAMETER_AUTHORIZATION = "authorization";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String BUCKET_NAME = System.getenv("S3_BUCKET_NAME");
    private final static Logger logger = LoggerFactory.getLogger(Test.class);

    Decode decoder = new Decode();
    Hash hash = new Hash();
    AmazonS3 s3Client = AWSConfig.awss3Client();

    RandomStringGenerator random = new RandomStringGenerator();
    // get the images' details of a product
    @GetMapping("/{productId}/image")
    public ResponseEntity<List<Image>> getAllImagesOfAProduct(@PathVariable Integer productId, HttpServletRequest req) {
        // Logs and Metrics
        logger.info("This is the image - getAll api");
        statsDClient.incrementCounter("endpoint.image.http.getAll");
        //----------- check the auth -----------
        String authString = req.getHeader(AUTH_HEADER_PARAMETER_AUTHORIZATION);
        // username: authResult[0]  password: authResult[1]
        String[] authResult = decoder.decodeAuth(authString);
        String username = authResult[0];
        String password = authResult[1];
        // get user's id, check the auth
        // check the auth, if this update opeartion is by person who made it
        User user = userService.searchAUserByUsername(authResult[0]);
        if (user == null || !hash.checkPassword(password, user.getPassword())) {
            System.out.println(0);
            return new ResponseEntity<List<Image>>(HttpStatus.UNAUTHORIZED);
        }
        int userId = user.getId();
        // get the product
        Product returnProduct = productService.getAProduct(productId);
        if (returnProduct == null) {
            System.out.println(1);
            return new ResponseEntity<List<Image>>(HttpStatus.BAD_REQUEST);
        }
        // check if the product is created by the user, only this person can update it
        if (!Objects.equals(returnProduct.getOwner_user_id(), userId)) {
            System.out.println(2);
            return new ResponseEntity<List<Image>>(HttpStatus.UNAUTHORIZED);
        }
        // ----------- get the images -----------
        List<Image> images = imageService.getAllImagesOfAProduct(productId);
        return ResponseEntity.ok(images);
    }

    // Get an image details
    @GetMapping("/{productId}/image/{imageId}")
    public ResponseEntity<List<Image>> getAnImageOfAProduct(@PathVariable Integer productId, @PathVariable Integer imageId, HttpServletRequest req) {
        // Logs and Metrics
        logger.info("This is the image - getOne api");
        statsDClient.incrementCounter("endpoint.image.http.getOne");
        //----------- check the auth -----------
        String authString = req.getHeader(AUTH_HEADER_PARAMETER_AUTHORIZATION);
        // username: authResult[0]  password: authResult[1]
        String[] authResult = decoder.decodeAuth(authString);
        String username = authResult[0];
        String password = authResult[1];
        // get user's id, check the auth
        // check the auth, if this update opeartion is by person who made it
        User user = userService.searchAUserByUsername(authResult[0]);
        if (user == null || !hash.checkPassword(password, user.getPassword())) {
            System.out.println(0);
            return new ResponseEntity<List<Image>>(HttpStatus.UNAUTHORIZED);
        }
        int userId = user.getId();
        // get the product
        Product returnProduct = productService.getAProduct(productId);
        if (returnProduct == null) {
            System.out.println(1);
            return new ResponseEntity<List<Image>>(HttpStatus.BAD_REQUEST);
        }
        // check if the product is created by the user, only this person can update it
        if (!Objects.equals(returnProduct.getOwner_user_id(), userId)) {
            System.out.println(2);
            return new ResponseEntity<List<Image>>(HttpStatus.UNAUTHORIZED);
        }
        // ----------- get the image -----------
        List<Image> images = imageService.getAnImageOfAProduct(productId, imageId);
        if (images == null || images.size() == 0) {
            return new ResponseEntity<List<Image>>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(images);
    }

    //
    @PostMapping("/{productId}/image")
    public ResponseEntity<Image> uploadAnImage(@PathVariable Integer productId, @RequestParam("file") MultipartFile file, HttpServletRequest req) throws IOException {
        // Logs and Metrics
        logger.info("This is the image - post");
        statsDClient.incrementCounter("endpoint.image.http.post");
        //----------- check the auth -----------
        String authString = req.getHeader(AUTH_HEADER_PARAMETER_AUTHORIZATION);
        // username: authResult[0]  password: authResult[1]
        String[] authResult = decoder.decodeAuth(authString);
        String username = authResult[0];
        String password = authResult[1];
        // get user's id, check the auth
        // check the auth, if this update opeartion is by person who made it
        User user = userService.searchAUserByUsername(authResult[0]);
        if (user == null || !hash.checkPassword(password, user.getPassword())) {
            System.out.println(0);
            return new ResponseEntity<Image>(HttpStatus.UNAUTHORIZED);
        }
        int userId = user.getId();
        // get the product
        Product returnProduct = productService.getAProduct(productId);
        if (returnProduct == null) {
            System.out.println(1);
            return new ResponseEntity<Image>(HttpStatus.BAD_REQUEST);
        }
        // check if the product is created by the user, only this person can update it
        if (!Objects.equals(returnProduct.getOwner_user_id(), userId)) {
            System.out.println(2);
            return new ResponseEntity<Image>(HttpStatus.UNAUTHORIZED);
        }
        // upload the image to AWS
        String fileName = random.generateRandomString() + "/" +file.getOriginalFilename();
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME,  fileName, file.getInputStream(), null);
        try {
            s3Client.putObject(request);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
            return new ResponseEntity<Image>(HttpStatus.BAD_REQUEST);
        }
        // store the image(details) to rds
        Image newImage = new Image();
        // set the image file name
        newImage.setFile_name(fileName);
        // set the product id
        newImage.setProduct_id(productId);
        // set the s3 bucket path
        newImage.setS3_bucket_path(BUCKET_NAME + fileName);
        // set the date
        Date date = new Date();
        newImage.setDate_created(sdf.format(new Timestamp(date.getTime())));
        Image createdImage = imageService.uploadAnImage(newImage);
        return new ResponseEntity<Image>(createdImage, HttpStatus.CREATED);
    }

    // delete an image
    @DeleteMapping("/{productId}/image/{imageId}")
    public ResponseEntity<Image> deleteAImage(@PathVariable Integer productId, @PathVariable Integer imageId, HttpServletRequest req) {
        // Logs and Metrics
        logger.info("This is the image - delete");
        statsDClient.incrementCounter("endpoint.image.http.delete");
        //----------- check the auth -----------
        String authString = req.getHeader(AUTH_HEADER_PARAMETER_AUTHORIZATION);
        // username: authResult[0]  password: authResult[1]
        String[] authResult = decoder.decodeAuth(authString);
        String username = authResult[0];
        String password = authResult[1];
        // get user's id, check the auth
        // check the auth, if this update opeartion is by person who made it
        User user = userService.searchAUserByUsername(authResult[0]);
        if (user == null || !hash.checkPassword(password, user.getPassword())) {
            System.out.println(0);
            return new ResponseEntity<Image>(HttpStatus.UNAUTHORIZED);
        }
        int userId = user.getId();
        // get the product
        Product returnProduct = productService.getAProduct(productId);
        if (returnProduct == null) {
            System.out.println(1);
            return new ResponseEntity<Image>(HttpStatus.BAD_REQUEST);
        }
        // check if the product is created by the user, only this person can update it
        if (!Objects.equals(returnProduct.getOwner_user_id(), userId)) {
            System.out.println(2);
            return new ResponseEntity<Image>(HttpStatus.UNAUTHORIZED);
        }
        // check if the image exist
        List<Image> returnImage = imageService.getAnImageOfAProduct(productId, imageId);
        if (returnImage == null || returnImage.size() == 0) {
            System.out.println(3);
            return new ResponseEntity<Image>(HttpStatus.BAD_REQUEST);
        }
        // delete the image from s3
        try {
            s3Client.deleteObject(BUCKET_NAME, returnImage.get(0).getFile_name());
        } catch (AmazonServiceException e) {
            e.printStackTrace();
            return new ResponseEntity<Image>(HttpStatus.BAD_REQUEST);
        }
        // delete the image details from rds
        imageService.deleteAnImage(imageId);
        return new ResponseEntity<Image>(HttpStatus.NO_CONTENT);
    }

}
