package com.registrationlogindemo.controller;

import com.registrationlogindemo.model.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home/")
public class HomeController {
    @GetMapping("/")
    public String registrationForm(Model model) {
        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        model.addAttribute("userdetails",userDetails);
        return "home";
    }
}