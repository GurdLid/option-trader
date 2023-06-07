package com.optiontrader.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class StockPriceControllerTest {
    /**
     * This is a test class to test the controller for the Stock Price display page
     */
    @Autowired
    private StockPriceController stockPriceController;

    @Test //Basic Test to see if the controller loads correctly
    public void contextLoads() throws Exception {
        assertThat(stockPriceController).isNotNull();
    }


}