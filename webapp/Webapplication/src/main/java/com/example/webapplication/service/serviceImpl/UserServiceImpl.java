package com.example.webapplication.service.serviceImpl;


import com.example.webapplication.dao.UserRepository;
import com.example.webapplication.entity.User;
import com.example.webapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRep;

    @Override
    public User createAUser(User user) {
        User createdUser = userRep.save(user);
        return createdUser;
    }

    @Override
    public void updateAUser(User user) {
        userRep.save(user);
        return ;
    }


    @Override
    public User searchAUserByUsername(String username) {
        return userRep.searchAUserByUsername(username);
    }

    @Override
    public User searchAUserById(Integer userId) {
        return userRep.searchAUserById(userId);
    }
}