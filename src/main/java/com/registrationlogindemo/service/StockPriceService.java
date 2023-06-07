package com.registrationlogindemo.service;

import com.registrationlogindemo.dto.OptionDto;
import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.StockPrice;

import java.util.List;

public interface StockPriceService {
    //Basic CRUD operation to get all stock prices until today's date for displaying
    List<StockPrice> getAllStockPricesToToday();
}
