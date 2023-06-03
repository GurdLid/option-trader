package com.registrationlogindemo.controller;

import com.registrationlogindemo.dto.OptionDto;
import com.registrationlogindemo.model.CustomUserDetails;
import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.service.OptionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/home/")
@CrossOrigin
public class HomeController {
    @Autowired
    OptionService optionService;

    //Set<ConstraintViolation<Option>> violations = new HashSet<>();

    @GetMapping("/")
    public String registrationForm(Model model) {
        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        model.addAttribute("userdetails",userDetails);

        //long userId = userDetails.getId();

        //List<Option> options = optionService.getAllOptionsByUser(userId);
        //model.addAttribute(options);

        return "home";
    }
/*
    @GetMapping("purchase")
    public String buyOption()
    {
        return "addOption.html";
    }


    @PostMapping("addOption")
    public String addOption(HttpServletRequest request) {
        String strikePrice = request.getParameter("strikePrice");
        String expiryDate = request.getParameter("expiryDate");
        String optionType = request.getParameter("optionType");

        Option option = new Option();
        if(!strikePrice.isBlank()) {
            option.setStrikePrice(new BigDecimal(strikePrice));
        }
        else
        {
            option.setStrikePrice(new BigDecimal("99.99999"));
        }

        option.setExpiryDate(LocalDate.parse(expiryDate));
        option.setPurchaseDate(LocalDate.now());
        if(optionType.contains("Put"))
        {
            option.setOptionType(false);
        }
        else {
            option.setOptionType(true);
        }

        optionService.calculateOptionPrice(option);

        Validator validate = (Validator) Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(option);

        System.out.println(violations);

        if(violations.isEmpty()) {

        }

        return "redirect:/home";
    }


 */

}