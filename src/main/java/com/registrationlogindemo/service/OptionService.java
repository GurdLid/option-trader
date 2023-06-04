package com.registrationlogindemo.service;

import com.registrationlogindemo.dto.OptionDto;
import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

public interface OptionService {

    void calculateOptionPrice(Option option);
    void calculateOptionPrice(OptionDto option);

    List<LocalDate> possibleExpiryDates();

    void addOption(OptionDto optionDto);
}
