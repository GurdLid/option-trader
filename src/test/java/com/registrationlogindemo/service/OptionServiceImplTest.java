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


    @BeforeEach
    void setUp(){
        //Cleaning all database objects
        optionRepository.deleteAll(); //Deleting the Options
        List<User> users = userRepository.findAll(); //Deleting the users
        for(User user: users)
        {
            userService.deleteUser(user.getId());
        }
    }


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

        Option option = new Option();
        option.setOwner(userDto);
        option.setOptionType(true); //Call option
        option.setStrikePrice(new BigDecimal("120"));
        option.setPurchaseDate(LocalDate.now());
        option.setPrice(new BigDecimal("7.50"));
        option.setExpiryDate(LocalDate.parse("2024-12-12"));
        optionRepository.save(option);

        List<Option> options = optionRepository.findByOwner(userDto);

        assertEquals(new BigDecimal("120"), options.get(0).getStrikePrice());
        assertEquals(LocalDate.parse("2024-12-12"), options.get(0).getExpiryDate());
    }


    @Test
    public void testGetActiveOptions(){
        String email = "test@gmail.com";
        User userDto = new User();
        userDto.setName("test");
        userDto.setEmail(email);
        userDto.setPassword("pass");
        userDto.setBalance(new BigDecimal("100"));
        userService.saveUser(userDto);
        userDto.setId(userRepository.findByEmail(email).getId());

        Option option = new Option();
        option.setOwner(userDto);
        option.setOptionType(true); //Call option
        option.setStrikePrice(new BigDecimal("120"));
        option.setPurchaseDate(LocalDate.now());
        option.setPrice(new BigDecimal("7.50"));
        option.setExpiryDate(LocalDate.parse("2024-12-12"));
        optionRepository.save(option);

        Option option2 = new Option();
        option2.setOwner(userDto);
        option2.setOptionType(true); //Call option
        option2.setStrikePrice(new BigDecimal("130"));
        option2.setPurchaseDate(LocalDate.now());
        option2.setPrice(new BigDecimal("9.50"));
        option2.setExpiryDate(LocalDate.parse("2022-12-12"));
        option.setResolved(true);
        optionRepository.save(option2);

        List<Option> options = optionRepository.findByOwner(userDto);
        List<Option> activeOptions = optionService.getActiveOptions(options);

        assertEquals(1,activeOptions.size());
        assertEquals(LocalDate.parse("2022-12-12"),activeOptions.get(0).getExpiryDate());
    }

    @Test
    public void testOptionPriceCalculation() //test of accuracy, it should be within a certain range
    {
        String email = "test@gmail.com";
        User userDto = new User();
        userDto.setName("test");
        userDto.setEmail(email);
        userDto.setPassword("pass");
        userDto.setBalance(new BigDecimal("100"));
        userService.saveUser(userDto);
        userDto.setId(userRepository.findByEmail(email).getId());

        Option option = new Option();
        option.setOwner(userDto);
        option.setOptionType(true); //Call option
        option.setStrikePrice(new BigDecimal("120"));
        option.setPrice(new BigDecimal("0.0"));
        option.setPurchaseDate(LocalDate.parse("2023-06-06"));
        option.setExpiryDate(LocalDate.parse("2025-06-06"));
        optionRepository.save(option);

        optionService.calculateOptionPrice(option);
        BigDecimal actualPrice = new BigDecimal("45.66"); //this is the actual price from an online calc (goodcalculators.com)
        BigDecimal lowerToleranceBound = new BigDecimal("45.2");
        BigDecimal upperToleranceBound = new BigDecimal("45.9");

        assertEquals(1,option.getPrice().compareTo(lowerToleranceBound));
        assertEquals(-1,option.getPrice().compareTo(upperToleranceBound));
    }


}