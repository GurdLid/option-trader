package com.registrationlogindemo.controller;

import com.registrationlogindemo.dto.OptionDto;
import com.registrationlogindemo.model.CustomUserDetails;
import com.registrationlogindemo.model.Option;
import com.registrationlogindemo.model.User;
import com.registrationlogindemo.service.OptionService;
import com.registrationlogindemo.service.UserService;
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

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String homePage(Model model) {
        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        model.addAttribute("userdetails",userDetails);

        User user = userService.findUserByEmail(userDetails.getUsername());
        //resolving todays options
        List<Option> options = user.getOptions();
        List<Option> activeOptions = optionService.getActiveOptions(options); //this will include todays options as they have yet to resolve and so have profit == null, resolved == false

        optionService.resolveTodaysOptionOutcomes(activeOptions); //todays options now have aa profit column
        List<Option> expiredOptions = optionService.getExpiredOptions(user.getOptions());

        //Displays options after resolving
        model.addAttribute("activeOptions",activeOptions);
        model.addAttribute("expiredOptions",expiredOptions);

        //Now need to apply today's profit to the balance
        userService.resolveTodaysBalance(user,activeOptions); //Balance is updated
        BigDecimal balance = user.getBalance();
        model.addAttribute("balance",balance);

        userService.saveUser(user);

        return "home";
    }





}