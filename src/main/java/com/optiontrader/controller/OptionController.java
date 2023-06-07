package com.optiontrader.controller;

import com.optiontrader.dto.OptionDto;
import com.optiontrader.model.CustomUserDetails;
import com.optiontrader.model.StockPrice;
import com.optiontrader.model.User;
import com.optiontrader.service.OptionService;
import com.optiontrader.service.StockPriceService;
import com.optiontrader.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OptionController {
    /**
     * Controller for purchasing an OPTION
     */
    @Autowired
    private StockPriceService stockPriceService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private UserService userService;

    @RequestMapping("buyoption")
    public String buyOptionPage(Model model) {
        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal(); //Getting details of current logged in user

        String currentUserEmail = userDetails.getUsername();
        User currentUser = userService.findUserByEmail(currentUserEmail);
        BigDecimal currentBalance = currentUser.getBalance();
        model.addAttribute("balance",currentBalance); //Adding their balance to the model to be displayed

        OptionDto optionDto = new OptionDto();
        model.addAttribute("option", optionDto);

        List<LocalDate> expiryDates = optionService.possibleExpiryDates(); //Getting a list of potential expiry dates based on todays date.
        model.addAttribute("expiryDates", expiryDates);

        //this block of code retrieves the three most recent days of stock prices so the purchaser can make a more informed decision
        List<StockPrice> stockPrices = stockPriceService.getAllStockPricesToToday(); 
        List<StockPrice> lastThreePrices = new ArrayList<>();
        for(int i = 0; i<3; i++)
        {
            lastThreePrices.add(stockPrices.get(i));
        }
        model.addAttribute("lateststockprices", lastThreePrices);

        return "buyoption.html";
    }

    @PostMapping("buyoption")
    public String registration(
            @Valid @ModelAttribute("option") OptionDto optionDto,
            BindingResult result,
            Model model) {

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal(); //Getting current user info

        String currentUserEmail = userDetails.getUsername();
        User currentUser = userService.findUserByEmail(currentUserEmail);
        optionDto.setTraderId(Math.toIntExact(currentUser.getId()));
        optionDto.setPurchaseDate(LocalDate.now());
        optionService.calculateOptionPrice(optionDto); //calculating the option price from the entered information

        model.addAttribute("price",optionDto.getPrice());

        if (result.hasErrors()) {
            List<LocalDate> expiryDates = optionService.possibleExpiryDates();
            model.addAttribute("expiryDates", expiryDates);
            model.addAttribute("option", optionDto); //This validation is such that if there are errors, reload the page with error messages
            return "buyoption";
        }

        //Checking to see if the user could afford the option
        BigDecimal balanceBeforePurchase = currentUser.getBalance();
        userService.resolvePurchase(currentUser,optionDto);
        if(currentUser.getBalance().equals(balanceBeforePurchase)) // they could not afford the option
        {
            List<LocalDate> expiryDates = optionService.possibleExpiryDates();
            model.addAttribute("expiryDates", expiryDates);
            model.addAttribute("option", optionDto);
            return "buyoption";
        }

        optionService.addOption(optionDto); //only saving the option and user if all is successful
        userService.saveUser(currentUser);
        model.addAttribute("balance",currentUser.getBalance());

        return "redirect:/buyoption?success";
    }

}