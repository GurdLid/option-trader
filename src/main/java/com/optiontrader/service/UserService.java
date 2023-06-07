package com.optiontrader.service;

import com.optiontrader.dto.OptionDto;
import com.optiontrader.dto.UserDto;
import com.optiontrader.model.Option;
import com.optiontrader.model.User;

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