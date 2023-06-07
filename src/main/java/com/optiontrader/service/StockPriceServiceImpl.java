package com.optiontrader.service;

import com.optiontrader.repository.StockPriceRepository;
import com.optiontrader.model.StockPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StockPriceServiceImpl implements StockPriceService{
    /**
     * Implementation of the StockPriceService interface to display all stockprices into and including today's
     */
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
