package com.registrationlogindemo.controller;

import com.registrationlogindemo.dto.OptionDto;
import com.registrationlogindemo.dto.UserDto;
import com.registrationlogindemo.model.StockPrice;
import com.registrationlogindemo.model.User;
import com.registrationlogindemo.service.OptionService;
import com.registrationlogindemo.service.StockPriceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private StockPriceService stockPriceService;

    @Autowired
    private OptionService optionService;

    @RequestMapping("buyoption")
    public String buyOptionPage(Model model) {

        OptionDto optionDto = new OptionDto();
        model.addAttribute("option", optionDto);

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

        if (result.hasErrors()) {
            model.addAttribute("option", optionDto);
            return "/buyoption";
        }

        return "redirect:/buyoption?success";
    }

}