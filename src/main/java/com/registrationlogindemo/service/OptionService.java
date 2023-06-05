package com.registrationlogindemo.service;

import com.registrationlogindemo.dto.OptionDto;
import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OptionService {

    void calculateOptionPrice(Option option);
    void calculateOptionPrice(OptionDto option);

    List<LocalDate> possibleExpiryDates();

    List<Option> getActiveOptions(List<Option> allOptions);
    List<Option> getExpiredOptions(List<Option> allOptions);


    void resolveTodaysOptionOutcomes(List<Option> activeOptions);

    void addOption(OptionDto optionDto);

    void updateOption(OptionDto optionDto);
    void updateOption(Option option);

}
