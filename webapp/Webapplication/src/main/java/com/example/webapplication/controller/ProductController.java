package com.example.webapplication.controller;

import com.example.webapplication.entity.Product;
import com.example.webapplication.entity.User;
import com.example.webapplication.service.ProductService;
import com.example.webapplication.service.UserService;
import com.example.webapplication.utils.Decode;
import com.example.webapplication.utils.Hash;
import com.timgroup.statsd.StatsDClient;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/v1/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    private StatsDClient statsDClient;

    private static final String AUTH_HEADER_PARAMETER_AUTHORIZATION = "authorization";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static Logger logger = LoggerFactory.getLogger(Test.class);

    Decode decoder = new Decode();
    Hash hash = new Hash();

    @PostMapping
    public ResponseEntity<Product> addAProduct(@RequestBody Product product, HttpServletRequest req) {
        // Logs and Metrics
        logger.info("This is the product - post");
        statsDClient.incrementCounter("endpoint.product.http.post");

        String authString = req.getHeader(AUTH_HEADER_PARAMETER_AUTHORIZATION);
        // username: authResult[0]  password: authResult[1]
        String[] authResult = decoder.decodeAuth(authString);
        String username = authResult[0];
        String password = authResult[1];
        // get user
        User user = userService.searchAUserByUsername(authResult[0]);
        if (user == null || !hash.checkPassword(password, user.getPassword())) {
            System.out.println(1);
            return new ResponseEntity<Product>(HttpStatus.UNAUTHORIZED);
        }
        // check the required field
        if (product.getName() == null || product.getDescription() == null || product.getSku() == null || product.getManufacturer() == null || product.getQuantity() == null) {
            System.out.println(2);
            return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        }
        // check quantity
        if (product.getQuantity() < 0 || product.getQuantity() > 100 || product.getQuantity().getClass().equals(String.class)) {
            System.out.println(3);
            return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        }
        // check sku
        Product productBySku = productService.searchProductBySku(product.getSku());
        if (productBySku != null) {
            System.out.println(4);
            return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        }
        // set the userId
        int userId = user.getId();
        product.setOwner_user_id(userId);
        // set the added and last updated time
        Date date = new Date();
        product.setDate_added(sdf.format(new Timestamp(date.getTime())));
        product.setDate_last_updated(sdf.format(new Timestamp(date.getTime())));
        // add this product
        Product createdProduct = productService.addAProduct(product);
        return new ResponseEntity<Product>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product>  updateProduct(@PathVariable Integer productId, @RequestBody Product product, HttpServletRequest req) {
        // Logs and Metrics
        logger.info("This is the product - put");
        statsDClient.incrementCounter("endpoint.product.http.put");

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
            return new ResponseEntity<Product>(HttpStatus.UNAUTHORIZED);
        }
        int userId = user.getId();
        // get the product
        Product returnProduct = productService.getAProduct(productId);
        if (returnProduct == null) {
            System.out.println(1);
            return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        }
        // check if the product is created by the user, only this person can update it
        if (!Objects.equals(returnProduct.getOwner_user_id(), userId)) {
            System.out.println(2);
            return new ResponseEntity<Product>(HttpStatus.UNAUTHORIZED);
        }
        // check the required field
        if (product.getName() == null || product.getDescription() == null || product.getSku() == null || product.getManufacturer() == null || product.getQuantity() == null) {
            System.out.println(3);
            return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        }
        // check quantity
        if (product.getQuantity() < 0 || product.getQuantity() > 100 || product.getQuantity().getClass().equals(String.class)) {
            System.out.println(4);
            return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        }
        // check sku
        Product productBySku = productService.searchProductBySku(product.getSku());
        if (productBySku!= null && !Objects.equals(productId, productBySku.getId())) {
            System.out.println(5);
            return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        }
        // update the product
        // set the productId
        product.setId(productId);
        // set the updated time
        Date date = new Date();
        product.setDate_last_updated(sdf.format(new Timestamp(date.getTime())));
        product.setDate_added(returnProduct.getDate_added());
        // set owner user id
        product.setOwner_user_id(returnProduct.getOwner_user_id());
        productService.updateAProduct(product);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<Product>  updateAProduct(@PathVariable Integer productId, @RequestBody Product product, HttpServletRequest req) {
        // Logs and Metrics
        logger.info("This is the product - patch");
        statsDClient.incrementCounter("endpoint.product.http.patch");

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
            return new ResponseEntity<Product>(HttpStatus.UNAUTHORIZED);
        }
        int userId = user.getId();
        // get the product
        Product returnProduct = productService.getAProduct(productId);
        if (returnProduct == null) {
            System.out.println(1);
            return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        }
        // check if the product is created by the user, only this person can update it
        if (!Objects.equals(returnProduct.getOwner_user_id(), userId)) {
            System.out.println(2);
            return new ResponseEntity<Product>(HttpStatus.UNAUTHORIZED);
        }
        // check the required field
        if (product.getName() == null || product.getDescription() == null || product.getSku() == null || product.getManufacturer() == null || product.getQuantity() == null) {
            System.out.println(3);
            return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        }
        // check quantity
        if (product.getQuantity() < 0 || product.getQuantity() > 100 || product.getQuantity().getClass().equals(String.class)) {
            System.out.println(4);
            return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        }
        // check sku
        Product productBySku = productService.searchProductBySku(product.getSku());
        if (productBySku!= null && !Objects.equals(productId, productBySku.getId())) {
            System.out.println(5);
            return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        }
        // update the product
        // set the productId
        product.setId(productId);
        // set the updated time
        Date date = new Date();
        product.setDate_last_updated(sdf.format(new Timestamp(date.getTime())));
        product.setDate_added(returnProduct.getDate_added());
        // set owner user id
        product.setOwner_user_id(returnProduct.getOwner_user_id());
        productService.updateAProduct(product);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product>  deleteAProduct(@PathVariable Integer productId, HttpServletRequest req) {
        // Logs and Metrics
        logger.info("This is the product - delete");
        statsDClient.incrementCounter("endpoint.product.http.delete");

        // check auth
        // get user's id, check the auth
        String authString = req.getHeader(AUTH_HEADER_PARAMETER_AUTHORIZATION);
        // username: authResult[0]  password: authResult[1]
        String[] authResult = decoder.decodeAuth(authString);
        String username = authResult[0];
        String password = authResult[1];
        // check if the product exists, if it does exist, we can not delete it
        Product productResult = productService.getAProduct(productId);
        if (productResult == null) {
            return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        }
        // check the auth, if this update opeartion is by person who made it
        User user = userService.searchAUserByUsername(authResult[0]);
        if (user == null || !hash.checkPassword(password, user.getPassword())) {
            System.out.println(0);
            return new ResponseEntity<Product>(HttpStatus.UNAUTHORIZED);
        }

        int userId = user.getId();
        // get the product
        Product returnProduct = productService.getAProduct(productId);

        if (returnProduct == null) {
            System.out.println(1);
            return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        }
        if (!Objects.equals(returnProduct.getOwner_user_id(), userId)) {
            System.out.println(2);
            return new ResponseEntity<Product>(HttpStatus.UNAUTHORIZED);
        }
        productService.deleteAProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getAProduct(@PathVariable Integer productId, HttpServletRequest req) {
        // Logs and Metrics
        logger.info("This is the product - get");
        statsDClient.incrementCounter("endpoint.product.http.get");

        // check auth
        // get user's id, check the auth
        String authString = req.getHeader(AUTH_HEADER_PARAMETER_AUTHORIZATION);
        // username: authResult[0]  password: authResult[1]
        String[] authResult = decoder.decodeAuth(authString);
        String username = authResult[0];
        String password = authResult[1];
        // check the auth, if this update opeartion is by person who made it
        User user = userService.searchAUserByUsername(authResult[0]);
        if (user == null || !hash.checkPassword(password, user.getPassword())) {
            System.out.println(0);
            return new ResponseEntity<Product>(HttpStatus.UNAUTHORIZED);
        }
        //
        Product productResult = productService.getAProduct(productId);
        if (productResult == null) {
            System.out.println(1);
            return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
        }
        if (productResult.getOwner_user_id() != user.getId()) {
            System.out.println(2);
            return new ResponseEntity<Product>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(productResult);
    }

}
