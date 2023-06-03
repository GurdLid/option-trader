package com.registrationlogindemo.service;

import com.registrationlogindemo.model.Option;

import java.time.LocalDate;
import java.util.List;

public interface OptionService {

    void calculateOptionPrice(Option option);

    List<LocalDate> possibleExpiryDates();

}
