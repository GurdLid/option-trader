package com.registrationlogindemo.service;

import com.registrationlogindemo.dto.OptionDto;
import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.StockPrice;
import com.registrationlogindemo.repository.StockPriceRepository;
import com.registrationlogindemo.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StockPriceServiceImpl implements StockPriceService{
    @Autowired
    StockPriceRepository stockPriceRepository;

    @Override
    public List<StockPrice> getAllStockPricesToToday() {
        List<StockPrice> allStocks = stockPriceRepository.findAll();
        List<StockPrice> stockHistoryToToday = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for(StockPrice stockprice : allStocks)
        {
            if(stockprice.getDate().isBefore(today.plusDays(1))) { //only displaying up to and including today
                stockHistoryToToday.add(stockprice);
            }
        }
        Collections.reverse(stockHistoryToToday); //Making the most recent dates the first dates
        return stockHistoryToToday;
    }

}
