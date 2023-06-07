package com.optiontrader.repository;

import com.optiontrader.model.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice, LocalDate> {
    /**
     * Repository to get stock prices from the DB
     */
    StockPrice findByDate(LocalDate date); //Code to find a stockprice when given a date

}