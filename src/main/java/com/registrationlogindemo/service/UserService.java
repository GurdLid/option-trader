package com.registrationlogindemo.service;

import com.registrationlogindemo.dto.UserDto;
import com.registrationlogindemo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);
    void saveUser(User user);

    User findUserByEmail(String email);

    void resolveBalance(User user, BigDecimal optionPrice);
}