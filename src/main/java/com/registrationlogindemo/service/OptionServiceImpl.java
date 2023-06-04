package com.registrationlogindemo.service;

import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.User;
import com.registrationlogindemo.repository.OptionRepository;
import com.registrationlogindemo.repository.StockPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class OptionServiceImpl implements OptionService{
    @Autowired
    OptionRepository optionRepository;
    @Autowired
    StockPriceRepository priceRepository;

    //Characteristics related to the stock
    private static final BigDecimal RISK_FREE_RATE = new BigDecimal("0.05");
    private static final BigDecimal VOLATILITY = new BigDecimal("0.25");


    @Override
    public void calculateOptionPrice(Option option) {
        option.setPrice(new BigDecimal("69"));
    }

    public BigDecimal calculateTimePeriod(Option option)
    {
        BigDecimal optionPeriod = BigDecimal.valueOf(ChronoUnit.DAYS.between(option.getPurchaseDate(), option.getExpiryDate()));
        optionPeriod = optionPeriod.divide(new BigDecimal("365.0", MathContext.DECIMAL32), MathContext.DECIMAL32);
        return optionPeriod;
    }

    @Override
    public List<LocalDate> possibleExpiryDates() {
        LocalDate today = LocalDate.now();
        List<LocalDate> allDates = new ArrayList<>();
        allDates.add(today.plusDays(1)); //One day
        allDates.add(today.plusWeeks(1)); //One week
        allDates.add(today.plusMonths(1)); //One month
        allDates.add(today.plusMonths(6)); //Six months
        allDates.add(today.plusYears(1)); //One year
        allDates.add(today.plusYears(5)); //Five years
        allDates.add(today.plusYears(10)); //Ten years
        return allDates;
    }

    @Override
    public List<Option> getAllOptionsByUser(User user) {
        List<Option> options = optionRepository.findByOwner(user);
        System.out.println(options.get(0).getId());
        return null;
    }

}
