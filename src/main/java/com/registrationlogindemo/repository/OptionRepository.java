package com.registrationlogindemo.repository;

import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    //Possible find by trader id?
}