package com.registrationlogindemo.service;

import com.registrationlogindemo.dto.UserDto;
import com.registrationlogindemo.model.Role;
import com.registrationlogindemo.model.User;
import com.registrationlogindemo.repository.RoleRepository;
import com.registrationlogindemo.repository.UserRepository;
import com.registrationlogindemo.util.TbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserDto userDto) {
        Role role = roleRepository.findByName(TbConstants.Roles.USER);

        if (role == null)
            role = roleRepository.save(new Role(TbConstants.Roles.USER));

        User user = new User(userDto.getName(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()),
                userDto.getBalance(), Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void resolveBalance(User user, BigDecimal optionPrice)
    {
        BigDecimal currentBalance = user.getBalance();
        BigDecimal net = currentBalance.subtract(optionPrice, MathContext.DECIMAL32);
        if(net.compareTo(new BigDecimal("0.0"))==1) {
            user.setBalance(currentBalance.subtract(optionPrice, MathContext.DECIMAL32));
        }
        //If the net balance is more than 0, complete the purchase, else leave balance as it is.
    }



}