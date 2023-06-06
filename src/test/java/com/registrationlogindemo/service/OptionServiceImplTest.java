package com.registrationlogindemo.service;

import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.Role;
import com.registrationlogindemo.model.User;
import com.registrationlogindemo.repository.OptionRepository;
import com.registrationlogindemo.repository.RoleRepository;
import com.registrationlogindemo.repository.UserRepository;
import com.registrationlogindemo.util.TbConstants;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class OptionServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private StockPriceService stockPriceService;
    @Autowired
    private OptionService optionService;


    //@BeforeEach
    //void setUp(){
        //Cleaning all database objects
        //optionRepository.deleteAll(); //Deleting the Options
        //System.out.println("help 0");
        //List<User> users = userRepository.findAll(); //Deleting the users
        //for(User user: users)
        //{
            //userService.deleteUser(user.getId());
        //}
    //}

    //@Test
    //public void test(){
      //  assertEquals(3,3);
    //}

/*
    @Test
    public void testAddOption(){
        String email = "test@gmail.com";
        User userDto = new User();
        userDto.setName("test");
        userDto.setEmail(email);
        userDto.setPassword("pass");
        userDto.setBalance(new BigDecimal("100"));
        userService.saveUser(userDto);
        userDto.setId(userRepository.findByEmail(email).getId());

        System.out.println("help");

        Option option = new Option();
        option.setOwner(userDto);
        option.setOptionType(true); //Call option
        option.setStrikePrice(new BigDecimal("120"));
        option.setPurchaseDate(LocalDate.now());
        option.setPrice(new BigDecimal("7.50"));
        optionRepository.save(option);

        System.out.println("help 2");

        List<Option> options = optionRepository.findByOwner(userDto);
        System.out.println(options);


    }
*/
}