package com.example.webapplication.dao;

import com.example.webapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select * from user where user.id = ?1", nativeQuery = true)
    User searchAUserById(Integer userId);

    @Query(value = "select * from user where user.username = ?1", nativeQuery = true)
    User searchAUserByUsername(String username);
}
