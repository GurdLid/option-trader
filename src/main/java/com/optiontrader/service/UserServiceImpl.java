package com.optiontrader.service;

import com.optiontrader.dto.OptionDto;
import com.optiontrader.dto.UserDto;
import com.optiontrader.model.Option;
import com.optiontrader.model.Role;
import com.optiontrader.model.User;
import com.optiontrader.repository.OptionRepository;
import com.optiontrader.repository.RoleRepository;
import com.optiontrader.repository.UserRepository;
import com.optiontrader.util.TbConstants;
import jakarta.persistence.PreRemove;
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
    /**
     * Implementation of UserService Interface. Includes basic CRUD along with alterations to User's balance depending on option outcomes.
     */
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
        if(userDto.getEmail().contains("admin@ot.com")) //This is the only allowed admin account
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
        user.removeRole(role); //This method decouples the user and their role
        userRepository.save(user);
        roleRepository.save(role);
        userRepository.deleteById(id); //Then deletes the user
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override //Method to add any new profit to the user's balance
    public void resolveTodaysBalance(User user, List<Option> options) {
        for(Option option: options){
            if(option.getExpiryDate().equals(LocalDate.now()) && option.getResolved()==false){
                user.setBalance(user.getBalance().add(option.getProfit(), MathContext.DECIMAL32));
                option.setResolved(true); //option has been resolved (profit added to balance)
                optionRepository.save(option); //updating the option to the database
            }
        }
    }

    //method to check to see if a user can purchase an option
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