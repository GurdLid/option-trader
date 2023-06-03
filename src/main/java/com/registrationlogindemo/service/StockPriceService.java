package com.registrationlogindemo.service;

import com.registrationlogindemo.model.StockPrice;

import java.util.List;

public interface StockPriceService {
    List<StockPrice> getAllStockPricesToToday();
}
