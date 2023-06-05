package com.registrationlogindemo.controller;

import com.registrationlogindemo.dto.OptionDto;
import com.registrationlogindemo.dto.UserDto;
import com.registrationlogindemo.model.CustomUserDetails;
import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.StockPrice;
import com.registrationlogindemo.model.User;
import com.registrationlogindemo.service.OptionService;
import com.registrationlogindemo.service.StockPriceService;
import com.registrationlogindemo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class OptionController {
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
                        .getPrincipal();

        String currentUserEmail = userDetails.getUsername();
        User currentUser = userService.findUserByEmail(currentUserEmail);
        BigDecimal currentBalance = currentUser.getBalance();
        model.addAttribute("balance",currentBalance);


        OptionDto optionDto = new OptionDto();
        model.addAttribute("option", optionDto);

        List<LocalDate> expiryDates = optionService.possibleExpiryDates();
        model.addAttribute("expiryDates", expiryDates);

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
                        .getPrincipal();

        String currentUserEmail = userDetails.getUsername();
        User currentUser = userService.findUserByEmail(currentUserEmail);
        optionDto.setTraderId(Math.toIntExact(currentUser.getId()));
        optionDto.setPurchaseDate(LocalDate.now());
        optionService.calculateOptionPrice(optionDto);

        model.addAttribute("price",optionDto.getPrice());

        if (result.hasErrors()) {
            List<LocalDate> expiryDates = optionService.possibleExpiryDates();
            model.addAttribute("expiryDates", expiryDates);
            model.addAttribute("option", optionDto);
            return "buyoption";
        }

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