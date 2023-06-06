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
@CrossOrigin
public class AdminController {
    @Autowired
    OptionService optionService;

    @Autowired
    UserService userService;

    @GetMapping(value = {"admin/","admin"})
    public String homePage(Model model) {
        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        model.addAttribute("userdetails",userDetails);

        List<User> traders = userService.getAllUsers();
        User admin = userService.findUserByEmail("admin@ot.com");
        traders.remove(admin);
        model.addAttribute("traders", traders);

        return "admin";
    }


    @GetMapping("deleteUser")
    public String deleteUser(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        userService.deleteUser(id);

        return "redirect:/admin";
    }



}