package com.registrationlogindemo.service;

import com.registrationlogindemo.dto.OptionDto;
import com.registrationlogindemo.model.Option;

import java.time.LocalDate;
import java.util.List;

public interface OptionService {
    //Basic CRUD operations along with Option specific methods
    void calculateOptionPrice(Option option);
    void calculateOptionPrice(OptionDto option);
    List<LocalDate> possibleExpiryDates();
    List<Option> getActiveOptions(List<Option> allOptions);
    List<Option> getExpiredOptions(List<Option> allOptions);
    void resolveTodaysOptionOutcomes(List<Option> activeOptions);
    void addOption(OptionDto optionDto);
    void updateOption(OptionDto optionDto);
    void updateOption(Option option);
    void deleteAllOptions();
}
