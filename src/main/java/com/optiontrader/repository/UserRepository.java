package com.optiontrader.repository;


import com.optiontrader.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Repository to get User info by their email, id and get all
     */
    User findByEmail(String email);
    User findById(long id);
    List<User> findAll();
}