package com.registrationlogindemo.service;

import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

public interface OptionService {

    void calculateOptionPrice(Option option);

    List<LocalDate> possibleExpiryDates();

    public List<Option> getAllOptionsByUser(User user);


}
