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
import jakarta.persistence.PreRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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

        if (role == null){
            role = roleRepository.save(new Role(TbConstants.Roles.USER));
        }

        if(userDto.getEmail().contains("admin@ot.com"))
        {
            role = roleRepository.save(new Role(TbConstants.Roles.USER));
        }
        User user = new User(userDto.getName(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()),
                userDto.getBalance(), Arrays.asList(role));

        userRepository.save(user);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UserDto userDto)
    {
        User user = userRepository.findById(userDto.getId()).get();
        user.setName(userDto.getName());
        user.setPassword(userDto.getName()); //These are the only two things that should be edited
        userRepository.save(user); //Saving changes back to database
    }


    @Override
    @PreRemove
    public void deleteUser(long id) {
        User user = userRepository.findById(id);
        Role role = roleRepository.findByName(TbConstants.Roles.USER);
        user.removeRole(role);
        userRepository.save(user);
        roleRepository.save(role);
        userRepository.deleteById(id);
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