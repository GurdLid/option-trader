package com.registrationlogindemo.service;

import com.registrationlogindemo.dto.OptionDto;
import com.registrationlogindemo.dto.UserDto;
import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.User;
import com.registrationlogindemo.repository.OptionRepository;
import com.registrationlogindemo.repository.RoleRepository;
import com.registrationlogindemo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
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
            System.out.println(user.toString());
            userService.deleteUser(user.getId());
        }
    }

    @Test
    public void testUserCreation(){
        User userDto = new User();
        userDto.setName("test");
        userDto.setEmail("testereo@test.co.uk");
        userDto.setPassword("pass");
        userDto.setBalance(new BigDecimal("100"));
        userService.saveUser(userDto);

        userDto = userRepository.findByEmail("testereo@test.co.uk");
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

    @Test
    public void testDeleteById()
    {
        User userDto = new User();
        userDto.setName("test");
        userDto.setEmail("tester@test.co.uk");
        userDto.setPassword("pass");
        userDto.setBalance(new BigDecimal("100"));
        userService.saveUser(userDto);
        userDto.setId(userRepository.findByEmail("tester@test.co.uk").getId());

        userRepository.deleteById(userDto.getId());
        List<User> users = userRepository.findAll();
        assertFalse(users.contains(userDto));
    }

    @Test
    public void testFindByEmail()
    {
        String email = "test@gmail.com";

        User userDto = new User();
        userDto.setName("test");
        userDto.setEmail(email);
        userDto.setPassword("pass");
        userDto.setBalance(new BigDecimal("100"));
        userService.saveUser(userDto);
        userDto.setId(userRepository.findByEmail(email).getId());

        User actual = userRepository.findByEmail(email);
        assertEquals(email,actual.getEmail());
    }

    @Test
    public void testResolvePurchase() //test to see if a user can afford to buy an option
    {
        BigDecimal intialBalance = new BigDecimal("100");

        String email = "test@gmail.com";
        User userDto = new User();
        userDto.setName("test");
        userDto.setEmail(email);
        userDto.setPassword("pass");
        userDto.setBalance(intialBalance);
        userService.saveUser(userDto);
        userDto.setId(userRepository.findByEmail(email).getId());

        User userDto2 = new User();
        userDto2.setName("test");
        userDto2.setEmail("hello@gmail.com");
        userDto2.setPassword("pass");
        userDto2.setBalance(intialBalance);
        userService.saveUser(userDto2);
        userDto.setId(userRepository.findByEmail("hello@gmail.com").getId());

        BigDecimal optionPrice1 = new BigDecimal("50");
        BigDecimal optionPrice2 = new BigDecimal("150");

        OptionDto option1 = new OptionDto();
        option1.setPrice(optionPrice1);
        OptionDto option2 = new OptionDto();
        option2.setPrice(optionPrice2);

        userService.resolvePurchase(userDto,option1);
        userService.resolvePurchase(userDto2,option2);


        assertEquals(new BigDecimal("50"), userDto.getBalance());
        assertEquals(intialBalance, userDto2.getBalance()); //balance shold not change as this user cannot afford this option
    }


}