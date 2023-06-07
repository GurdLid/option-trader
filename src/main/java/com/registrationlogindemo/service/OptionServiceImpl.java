package com.registrationlogindemo.service;

import com.registrationlogindemo.dto.OptionDto;
import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.StockPrice;
import com.registrationlogindemo.model.User;
import com.registrationlogindemo.repository.OptionRepository;
import com.registrationlogindemo.repository.StockPriceRepository;
import com.registrationlogindemo.repository.UserRepository;
import com.registrationlogindemo.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class OptionServiceImpl implements OptionService{
    /**
     * Implementation of the OptionService interface,
     * providing methods to get all allowable expiry dates, polymorphic methods to calculate option prices along with some CRUD operatins
     */
    @Autowired
    OptionRepository optionRepository;
    @Autowired
    StockPriceRepository stockPriceRepository;
    @Autowired
    UserRepository userRepository;


    //Characteristics related to the stock
    private static final BigDecimal RISK_FREE_RATE = new BigDecimal("0.05");
    private static final BigDecimal VOLATILITY = new BigDecimal("0.25");

    @Override
    public List<LocalDate> possibleExpiryDates() {
        LocalDate today = LocalDate.now(); //The list of expiry dates is based on today (the purchase date)
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
    public void calculateOptionPrice(Option option) //The option price calculation methods are based on the BS formula, see references for more info
    {
        LocalDate purchase = option.getPurchaseDate();
        BigDecimal spotPrice = stockPriceRepository.findByDate(purchase).getPrice();

        BigDecimal timePeriod = calculateTimePeriod(option);

        BigDecimal d1 = BigDecimalUtil.ln(spotPrice.divide(option.getStrikePrice(), MathContext.DECIMAL32), 32);
        BigDecimal sigSqOverTwo = (VOLATILITY.pow(2, MathContext.DECIMAL32).divide(new BigDecimal("2.0", MathContext.DECIMAL32), MathContext.DECIMAL32));
        BigDecimal temp = sigSqOverTwo.add(RISK_FREE_RATE, MathContext.DECIMAL32);
        temp = temp.multiply(timePeriod, MathContext.DECIMAL32);
        d1 = d1.add(temp, MathContext.DECIMAL32);

        temp = (BigDecimalUtil.sqrt(timePeriod)).multiply(VOLATILITY, MathContext.DECIMAL32);
        d1 = d1.divide(temp, MathContext.DECIMAL32);

        BigDecimal d2 = d1.subtract(temp);
        if(option.isOptionType()) {

            BigDecimal leftTerm = (BigDecimalUtil.cdf(d1)).multiply(spotPrice, MathContext.DECIMAL32);
            BigDecimal rightTerm = (BigDecimalUtil.cdf(d2)).multiply(option.getStrikePrice(), MathContext.DECIMAL32);
            temp = (new BigDecimal("-1.0", MathContext.DECIMAL32)).multiply(RISK_FREE_RATE, MathContext.DECIMAL32).multiply(timePeriod, MathContext.DECIMAL32);
            temp = BigDecimalUtil.exp(temp, 32);
            rightTerm = rightTerm.multiply(temp, MathContext.DECIMAL32);

            option.setPrice(leftTerm.subtract(rightTerm, MathContext.DECIMAL32));
        }
        else {
            //Multiplying by -1 for the formula
            d1 = d1.multiply(new BigDecimal("-1.0",MathContext.DECIMAL32));
            d2 = d2.multiply(new BigDecimal("-1.0",MathContext.DECIMAL32));

            BigDecimal rightTerm = (BigDecimalUtil.cdf(d1)).multiply(spotPrice, MathContext.DECIMAL32);
            BigDecimal leftTerm = (BigDecimalUtil.cdf(d2)).multiply(option.getStrikePrice(), MathContext.DECIMAL32);
            temp = (new BigDecimal("-1.0", MathContext.DECIMAL32)).multiply(RISK_FREE_RATE, MathContext.DECIMAL32).multiply(timePeriod, MathContext.DECIMAL32);
            temp = BigDecimalUtil.exp(temp, 32);
            leftTerm = leftTerm.multiply(temp, MathContext.DECIMAL32);

            option.setPrice(leftTerm.subtract(rightTerm, MathContext.DECIMAL32));
        }
    }

    @Override
    public void calculateOptionPrice(OptionDto option)
    {
        LocalDate purchase = option.getPurchaseDate();
        StockPrice spot = stockPriceRepository.findByDate(purchase);
        BigDecimal spotPrice = spot.getPrice();

        BigDecimal timePeriod = calculateTimePeriod(option);

        BigDecimal d1 = BigDecimalUtil.ln(spotPrice.divide(option.getStrikePrice(), MathContext.DECIMAL32), 32);
        BigDecimal sigSqOverTwo = (VOLATILITY.pow(2, MathContext.DECIMAL32).divide(new BigDecimal("2.0", MathContext.DECIMAL32), MathContext.DECIMAL32));
        BigDecimal temp = sigSqOverTwo.add(RISK_FREE_RATE, MathContext.DECIMAL32);
        temp = temp.multiply(timePeriod, MathContext.DECIMAL32);
        d1 = d1.add(temp, MathContext.DECIMAL32);

        temp = (BigDecimalUtil.sqrt(timePeriod)).multiply(VOLATILITY, MathContext.DECIMAL32);
        d1 = d1.divide(temp, MathContext.DECIMAL32);

        BigDecimal d2 = d1.subtract(temp);
        if(option.isOptionType()) {

            BigDecimal leftTerm = (BigDecimalUtil.cdf(d1)).multiply(spotPrice, MathContext.DECIMAL32);
            BigDecimal rightTerm = (BigDecimalUtil.cdf(d2)).multiply(option.getStrikePrice(), MathContext.DECIMAL32);
            temp = (new BigDecimal("-1.0", MathContext.DECIMAL32)).multiply(RISK_FREE_RATE, MathContext.DECIMAL32).multiply(timePeriod, MathContext.DECIMAL32);
            temp = BigDecimalUtil.exp(temp, 32);
            rightTerm = rightTerm.multiply(temp, MathContext.DECIMAL32);

            option.setPrice(leftTerm.subtract(rightTerm, MathContext.DECIMAL32));
        }
        else {
            //Multiplying by -1 for the formula
            d1 = d1.multiply(new BigDecimal("-1.0",MathContext.DECIMAL32));
            d2 = d2.multiply(new BigDecimal("-1.0",MathContext.DECIMAL32));

            BigDecimal rightTerm = (BigDecimalUtil.cdf(d1)).multiply(spotPrice, MathContext.DECIMAL32);
            BigDecimal leftTerm = (BigDecimalUtil.cdf(d2)).multiply(option.getStrikePrice(), MathContext.DECIMAL32);
            temp = (new BigDecimal("-1.0", MathContext.DECIMAL32)).multiply(RISK_FREE_RATE, MathContext.DECIMAL32).multiply(timePeriod, MathContext.DECIMAL32);
            temp = BigDecimalUtil.exp(temp, 32);
            leftTerm = leftTerm.multiply(temp, MathContext.DECIMAL32);

            option.setPrice(leftTerm.subtract(rightTerm, MathContext.DECIMAL32));
        }
    }

    public BigDecimal calculateTimePeriod(Option option) //This method calculate the time between purchase and expiry date for the purposes of the price calculation
    {
        BigDecimal optionPeriod = BigDecimal.valueOf(ChronoUnit.DAYS.between(option.getPurchaseDate(), option.getExpiryDate()));
        optionPeriod = optionPeriod.divide(new BigDecimal("365.0", MathContext.DECIMAL32), MathContext.DECIMAL32);
        return optionPeriod;
    }

    public BigDecimal calculateTimePeriod(OptionDto option)
    {
        BigDecimal optionPeriod = BigDecimal.valueOf(ChronoUnit.DAYS.between(option.getPurchaseDate(), option.getExpiryDate()));
        optionPeriod = optionPeriod.divide(new BigDecimal("365.0", MathContext.DECIMAL32), MathContext.DECIMAL32);
        return optionPeriod;
    }

    @Override
    public void addOption(OptionDto optionDto){ //Add an option to the user (when they select purchase option)
        Option option = new Option();

        long traderId = optionDto.getTraderId(); //Finding the user
        User user = userRepository.findById(traderId);

        option.setPrice(optionDto.getPrice());
        option.setOptionType(optionDto.isOptionType());
        option.setOwner(user);
        option.setExpiryDate(optionDto.getExpiryDate());
        option.setPurchaseDate(optionDto.getPurchaseDate());
        option.setStrikePrice(optionDto.getStrikePrice());

        optionRepository.save(option); //Saving to DB
    }

    @Override
    public void updateOption(OptionDto optionDto){
        Option option = new Option();

        long traderId = optionDto.getTraderId();
        User user = userRepository.findById(traderId);

        option.setPrice(optionDto.getPrice());
        option.setOptionType(optionDto.isOptionType());
        option.setOwner(user);
        option.setExpiryDate(optionDto.getExpiryDate());
        option.setPurchaseDate(optionDto.getPurchaseDate());
        option.setStrikePrice(optionDto.getStrikePrice());

        optionRepository.save(option);
    }

    @Override
    public void updateOption(Option option){
        optionRepository.save(option);
    }

    @Override
    public void deleteAllOptions()
    {
        optionRepository.deleteAll();
    }


    @Override //Method to filter only active options when given a list of all options
    public List<Option> getActiveOptions(List<Option> allOptions){
        List<Option> activeOptions = new ArrayList<>();
        for(Option option: allOptions)
        {
            if(option.getResolved()==false) //If they have not been resolved, they are active
            {
                activeOptions.add(option);
            }
        }
        return activeOptions;
    }

    @Override
    public List<Option> getExpiredOptions(List<Option> allOptions){
        List<Option> expiredOptions = new ArrayList<>();
        for(Option option: allOptions)
        {
            if(option.getResolved()==true) //If they have been resolved, they are expired
            {
                expiredOptions.add(option);
            }
        }
        return expiredOptions;
    }

    //This method is applied on user login. It checks to see if there are any unresolvd options with expiry date = today and add profit
    //to the User's account if there is any profit
    public void resolveTodaysOptionOutcomes(List<Option> activeOptions){
        for(Option option: activeOptions){
            if(option.getExpiryDate().equals(LocalDate.now())) //If the expiry date is today from active options
            {
                BigDecimal todaysPrice = stockPriceRepository.findByDate(LocalDate.now()).getPrice();
                BigDecimal net = todaysPrice.subtract(option.getStrikePrice(),MathContext.DECIMAL32); //Seeing if the option made profit
                if(net.compareTo(new BigDecimal("0.0"))==1) //i.e the net profit is more than £0
                {
                    option.setProfit(net);
                }
                else{
                    option.setProfit(new BigDecimal("0.0")); //no profit on this option
                }
                optionRepository.save(option); //Saving option to DB
            }
        }
    }

}
