package com.example.webapplication.controller;

import com.example.webapplication.entity.User;
import com.example.webapplication.service.UserService;
import com.example.webapplication.utils.Decode;
import com.example.webapplication.utils.EmailValidation;
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
@RequestMapping("/v2/user")
public class UserController {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String AUTH_HEADER_PARAMETER_AUTHORIZATION = "authorization";

    @Autowired
    UserService userService;

    @Autowired
    private StatsDClient statsDClient;

    private final static Logger logger = LoggerFactory.getLogger(Test.class);

    Hash hash = new Hash();
    EmailValidation ev = new EmailValidation();

    Decode decoder = new Decode();

    @PostMapping
    public ResponseEntity<User> createAUser(@RequestBody User user) {
        // Logs and Metrics
        logger.info("This is the user - post");
        statsDClient.incrementCounter("endpoint.user.http.post");

        // check if the username has existed
        User existedUser = userService.searchAUserByUsername(user.getUsername());
        String regexPattern = "^(.+)@(\\S+)$";
        if (existedUser != null || !ev.patternMatches(user.getUsername(), regexPattern)) {
            // if the username has existed, return 400
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
        // hash the password
        String hashedPassword = hash.hashPassword(user.getPassword());
        // store the hashed password in database
        user.setPassword(hashedPassword);
        // set the created and updated time
        Date date = new Date();
        user.setAccount_created(sdf.format(new Timestamp(date.getTime())));
        user.setAccount_updated(sdf.format(new Timestamp(date.getTime())));
        // created a new user
        User createdUser = userService.createAUser(user);
        // return 201 created successfully
        return new ResponseEntity<User>(createdUser, HttpStatus.CREATED);
    }
    @PutMapping("{userId}")
    public ResponseEntity<User> updateAUser(@PathVariable Integer userId, @RequestBody User user, HttpServletRequest req) {
        // Logs and Metrics
        logger.info("This is the user - put");
        statsDClient.incrementCounter("endpoint.user.http.put");

        String authString = req.getHeader(AUTH_HEADER_PARAMETER_AUTHORIZATION);
        // username: authResult[0]  password: authResult[1]
        String[] authResult = decoder.decodeAuth(authString);
        // get the username and password
        String username = authResult[0];
        String password = authResult[1];
        // check if the username exists
        User existedUser = userService.searchAUserById(userId);
        if (existedUser == null) {
            // if the username not existed, return 400
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
        // check if the password is correct ?
        //  hash the passed password, hash it and compare it with the password we store
        if (!user.getUsername().equals(existedUser.getUsername()) || !hash.checkPassword(password, existedUser.getPassword())) {
            return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
        }
        // if they want to update other parts, return 400
        if (user.getAccount_updated() != null || user.getAccount_created() != null) {
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
        // not change password
        if (!Objects.equals(user.getPassword(), existedUser.getPassword())) {
            // change password
            System.out.println(user.getPassword());
            String newHashPassword = hash.hashPassword(user.getPassword());
            user.setPassword(newHashPassword);
        } else {
            user.setPassword(existedUser.getPassword());
        }
        // change the updated time
        Date date = new Date();
        user.setAccount_updated(sdf.format(new Timestamp(date.getTime())));
        user.setAccount_created(existedUser.getAccount_created());
        user.setId(existedUser.getId());
        user.setUsername(username);
        userService.updateAUser(user);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("{userId}")
    public ResponseEntity<User> searchAUser(@PathVariable Integer userId, HttpServletRequest req) {
        // Logs and Metrics
        logger.info("This is the user - get");
        statsDClient.incrementCounter("endpoint.user.http.get");

        // check auth
        String authString = req.getHeader(AUTH_HEADER_PARAMETER_AUTHORIZATION);
        // username: authResult[0]  password: authResult[1]
        String[] authResult = decoder.decodeAuth(authString);
        // get the username and password
        String username = authResult[0];
        String password = authResult[1];
        User existedUser = userService.searchAUserByUsername(username);
        if (!userId.equals(existedUser.getId()) || !hash.checkPassword(password, existedUser.getPassword())) {
            return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
        }
        User userResult = userService.searchAUserById(userId);
        userResult.setPassword("");
        return new ResponseEntity<User>(userResult, HttpStatus.OK);
    }
}


