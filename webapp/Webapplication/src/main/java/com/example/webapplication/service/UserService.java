package com.example.webapplication.service;

import com.example.webapplication.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createAUser(User user);

    void updateAUser(User user);

    User searchAUserById(Integer userId);

    User searchAUserByUsername(String username);
}
