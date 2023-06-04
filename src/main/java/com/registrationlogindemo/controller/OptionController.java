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
        OptionDto optionDto = new OptionDto();

        model.addAttribute("option", optionDto);

        List<LocalDate> expiryDates = optionService.possibleExpiryDates();
        model.addAttribute("expiryDates", expiryDates);

        List<String> optionTypes = new ArrayList<>();
        optionTypes.add("Call");
        optionTypes.add("Put");
        model.addAttribute("optionTypes", optionTypes);
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

        System.out.println(result.toString());

        System.out.println(optionDto.toString());

        if (result.hasErrors()) {
            model.addAttribute("option", optionDto);
            return "buyoption";
        }



        return "redirect:/buyoption?success";
    }

}