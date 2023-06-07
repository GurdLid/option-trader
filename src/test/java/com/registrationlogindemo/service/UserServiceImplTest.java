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
    /**
     * This is a test class to test the User Service class, to ensure it is performing CRUD applications along with business logic correctly
     */
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


    @BeforeEach //deleting all created entities before each run
    void setUp(){
        List<User> users = userRepository.findAll(); //Getting all users
        for(User user: users)
        {
            userService.deleteUser(user.getId()); //Decoupling their roles, then deleting them
        }
    }

    @Test //Method to test creating a new user
    public void testUserCreation(){
        //Creating user object and initialising variables
        User userDto = new User();
        userDto.setName("test");
        userDto.setEmail("testereo@test.co.uk");
        userDto.setPassword("pass");
        userDto.setBalance(new BigDecimal("100"));
        userService.saveUser(userDto); //Saving to database

        userDto = userRepository.findByEmail("testereo@test.co.uk"); //Retrieving the full user, with the generated ID
        Optional<User> fromDB = userRepository.findById(userDto.getId());

        assertEquals(fromDB.get().getId(), userDto.getId()); //ensuring the ID match
    }

    @Test //test to get all of the users
    public void testGetAllUsers(){
        //Creating two users and saving them to the database
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

        //Getting a list of all users
        List<User> users = userRepository.findAll();
        //Checking to see if: 1) there are two users returned and 2) that the users returned are correct
        assertEquals(2, users.size(), "Incorrect number of users");
        assertTrue(users.contains(userDto));
        assertTrue(users.contains(user2));
    }

    @Test //test to delete a user by their ID
    public void testDeleteById()
    {
        //Creating and initialising a user object
        User userDto = new User();
        userDto.setName("test");
        userDto.setEmail("tester@test.co.uk");
        userDto.setPassword("pass");
        userDto.setBalance(new BigDecimal("100"));
        userService.saveUser(userDto);
        userDto.setId(userRepository.findByEmail("tester@test.co.uk").getId());

        userRepository.deleteById(userDto.getId()); //Deleting the user
        List<User> users = userRepository.findAll(); //Getting a list of all users
        assertFalse(users.contains(userDto)); //Ensuring the deleted user is not present
    }

    @Test //Method to test finding a user by their email address
    public void testFindByEmail()
    {
        //Creating and initialising a user object
        String email = "test@gmail.com";
        User userDto = new User();
        userDto.setName("test");
        userDto.setEmail(email);
        userDto.setPassword("pass");
        userDto.setBalance(new BigDecimal("100"));
        userService.saveUser(userDto); //Saving to DB
        userDto.setId(userRepository.findByEmail(email).getId()); //retrieving full user object, with ID

        User actual = userRepository.findByEmail(email);
        assertEquals(email,actual.getEmail()); //Ensuring email addresses match
    }

    @Test //Test to see if a user can afford to buy an option
    public void testResolvePurchase()
    {
        //Initialising 2 users with balances of 100
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

        //Creating two options, one with price = 50, other with price = 150
        BigDecimal optionPrice1 = new BigDecimal("50");
        BigDecimal optionPrice2 = new BigDecimal("150");

        OptionDto option1 = new OptionDto();
        option1.setPrice(optionPrice1);
        OptionDto option2 = new OptionDto();
        option2.setPrice(optionPrice2);

        //Calling the method
        userService.resolvePurchase(userDto,option1);
        userService.resolvePurchase(userDto2,option2);

        //This first assert implies the purchase was successful: they had balance of 100, option is 50, 100-50=50
        assertEquals(new BigDecimal("50"), userDto.getBalance());
        assertEquals(intialBalance, userDto2.getBalance()); //balance should not change as this user cannot afford this option
    }

    @Test //Test to update a user's information
    public void testUpdateUser()
    {
        //Creating a user and initialising vars
        User userDto2 = new User();
        userDto2.setName("tester");
        userDto2.setEmail("tester@test.co.uk");
        userDto2.setPassword("pass");
        userDto2.setBalance(new BigDecimal("100"));
        userService.saveUser(userDto2); //saving to database

        //Creating a dto object with updated information for name
        UserDto userDto = new UserDto();
        userDto.setId(userDto2.getId());
        userDto.setName("test");
        userDto.setEmail("tester@test.co.uk");
        userDto.setPassword("pass");
        userDto.setBalance(new BigDecimal("100"));

        userService.updateUser(userDto); //updating the user
        User fromDB = userRepository.findByEmail("tester@test.co.uk"); //getting the updated user object

        assertEquals("test", fromDB.getName()); //Ensuring the new name is what we changed it to
    }
}