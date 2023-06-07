package com.optiontrader.controller;

import com.optiontrader.model.CustomUserDetails;
import com.optiontrader.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {
    /**
     * This is a test class to test the controller for the User home page
     */
    @Autowired
    private HomeController homeController;

    @Test //Test to see if the controller loads correctly
    public void contextLoads() throws Exception {
        assertThat(homeController).isNotNull();
    }

    @Test //Test for the user's login information
    public void testForCorrectUserInfo() throws Exception {
        //Creating the user
        User user = new User();
        user.setName("test");
        user.setPassword("pass");
        user.setEmail("test@tester.com");
        user.setBalance(new BigDecimal("100"));
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(customUserDetails, null);

        //Using mockito to simulate the login authentication
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        CustomUserDetails userDetails =
                (CustomUserDetails)SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        assertEquals("test@tester.com", userDetails.getUsername()); //Checking that the current user has the expected details
    }


}