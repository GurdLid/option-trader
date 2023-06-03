package com.registrationlogindemo.controller;

import com.registrationlogindemo.dto.UserDto;
import com.registrationlogindemo.model.User;
import com.registrationlogindemo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            Model model) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null)
            result.rejectValue("email", null,
                    "User already registered");

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/registration";
        }

        if(userDto.getBalance() == null)
        {
            userDto.setBalance(new BigDecimal("0.0"));
        }

        userService.saveUser(userDto);
        return "redirect:/registration?success";
    }
}