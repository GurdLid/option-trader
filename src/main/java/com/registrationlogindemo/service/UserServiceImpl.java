package com.registrationlogindemo.service;

import com.registrationlogindemo.dto.OptionDto;
import com.registrationlogindemo.dto.UserDto;
import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.Role;
import com.registrationlogindemo.model.User;
import com.registrationlogindemo.repository.OptionRepository;
import com.registrationlogindemo.repository.RoleRepository;
import com.registrationlogindemo.repository.UserRepository;
import com.registrationlogindemo.util.TbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OptionRepository optionRepository;

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
    public void resolveTodaysBalance(User user, List<Option> options) {
        for(Option option: options){
            if(option.getExpiryDate().equals(LocalDate.now()) && option.getResolved()==false){
                user.setBalance(user.getBalance().add(option.getProfit(), MathContext.DECIMAL32));
                option.setResolved(true); //option has been resolved (profit added to balance)
                optionRepository.save(option); //updating the option to the database
            }
        }
    }

    public void resolvePurchase(User user, OptionDto option)
    {
        BigDecimal optionPrice = option.getPrice();
        if(optionPrice.compareTo(user.getBalance())==-1) //i.e. the option is worth less than the user's balance (they can purchase it)
        {
            user.setBalance(user.getBalance().subtract(optionPrice,MathContext.DECIMAL32));
            //Setting the new balance = old balance - option price
        }
        //Else the balance remains the same, option cannot be purchased
    }



}