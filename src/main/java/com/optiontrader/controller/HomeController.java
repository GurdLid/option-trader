package com.optiontrader.controller;

import com.optiontrader.model.Option;
import com.optiontrader.service.OptionService;
import com.optiontrader.service.UserService;
import com.optiontrader.model.CustomUserDetails;
import com.optiontrader.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;


@Controller
@RequestMapping("/home/")
@CrossOrigin
public class HomeController {
    /**
     * This class is the controller for the home page a user sees after a successful login, (like a dashboard)
     */
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
                        .getPrincipal(); //Getting details from current logged in user

        model.addAttribute("userdetails",userDetails);

        User user = userService.findUserByEmail(userDetails.getUsername()); //Creating a User object from the current logged in user's details
        //resolving today's options
        List<Option> options = user.getOptions();
        List<Option> activeOptions = optionService.getActiveOptions(options); //this will include todays options as they have yet to resolve and so have profit = null, resolved = false

        optionService.resolveTodaysOptionOutcomes(activeOptions); //todays options now have a profit column
        List<Option> expiredOptions = optionService.getExpiredOptions(user.getOptions()); //These options have resolved = true

        //Displays options after resolving
        model.addAttribute("activeOptions",activeOptions);
        model.addAttribute("expiredOptions",expiredOptions); //Adding to the model

        //Now need to apply today's profit to the balance
        userService.resolveTodaysBalance(user,activeOptions); //Balance is updated
        BigDecimal balance = user.getBalance();
        model.addAttribute("balance",balance);

        userService.saveUser(user); //Saving the user back to the DB

        return "home";
    }





}