package com.registrationlogindemo.service;

import com.registrationlogindemo.dto.OptionDto;
import com.registrationlogindemo.dto.UserDto;
import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.User;

import java.util.List;

public interface UserService {
    //Basic CRUD operations along with User balance operations
    void saveUser(UserDto userDto);
    void saveUser(User user);
    List<User> getAllUsers();
    public void updateUser(UserDto userDto);
    void deleteUser(long id);
    User findUserByEmail(String email);
    void resolveTodaysBalance(User user, List<Option> options);
    void resolvePurchase(User user, OptionDto option);
}