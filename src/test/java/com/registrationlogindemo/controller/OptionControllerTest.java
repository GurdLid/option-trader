package com.registrationlogindemo.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class OptionControllerTest {
    /**
     * This is a test class to test the controller for the Option buying page
     */
    @Autowired
    private OptionController optionController;

    @Test //Test to see if the controller loads correctly
    public void contextLoads() throws Exception {
        assertThat(optionController).isNotNull();
    }
}