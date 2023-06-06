package com.registrationlogindemo.service;

import com.registrationlogindemo.dto.UserDto;
import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.User;
import com.registrationlogindemo.repository.OptionRepository;
import com.registrationlogindemo.repository.RoleRepository;
import com.registrationlogindemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class UserServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private StockPriceService stockPriceService;

    @BeforeEach
    void setUp(){
        List<User> users = userRepository.findAll();
        for(User user: users)
        {
            userService.deleteUser(user.getId());
        }
    }


    @Test
    public void testUserCreation(){
        User userDto = new User();
        userDto.setName("test");
        userDto.setEmail("tester@test.co.uk");
        userDto.setPassword("pass");
        userDto.setBalance(new BigDecimal("100"));

        userService.saveUser(userDto);
        userDto.setId(userRepository.findByEmail("tester@test.co.uk").getId());

        Optional<User> fromDB = userRepository.findById(userDto.getId());
        assertEquals(fromDB.get().getId(), userDto.getId());
    }

    @Test
    public void testGetAllUsers(){
        User userDto = new User();
        userDto.setName("test");
        userDto.setEmail("tester@test.co.uk");
        userDto.setPassword("pass");
        userDto.setBalance(new BigDecimal("100"));
        userService.saveUser(userDto);
        userDto.setId(userRepository.findByEmail("tester@test.co.uk").getId());

        User user2 = new User();
        user2.setName("test2");
        user2.setEmail("tester2@test.co.uk");
        user2.setPassword("pass2");
        user2.setBalance(new BigDecimal("1002"));
        userService.saveUser(user2);
        user2.setId(userRepository.findByEmail("tester2@test.co.uk").getId());

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size(), "Incorrect number of users");
        assertTrue(users.contains(userDto));
        assertTrue(users.contains(user2));


    }

}