package com.optiontrader.service;

import com.optiontrader.model.StockPrice;

import java.util.List;

public interface StockPriceService {
    //Basic CRUD operation to get all stock prices until today's date for displaying
    List<StockPrice> getAllStockPricesToToday();
}
