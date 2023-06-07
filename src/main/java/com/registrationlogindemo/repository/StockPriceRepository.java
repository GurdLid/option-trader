package com.registrationlogindemo.repository;

import com.registrationlogindemo.model.Role;
import com.registrationlogindemo.model.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice, LocalDate> {
    /**
     * Repository to get stock prices from the DB
     */
    StockPrice findByDate(LocalDate date); //Code to find a stockprice when given a date

}