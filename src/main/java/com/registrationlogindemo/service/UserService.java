package com.registrationlogindemo.service;

import com.registrationlogindemo.dto.UserDto;
import com.registrationlogindemo.model.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);
}