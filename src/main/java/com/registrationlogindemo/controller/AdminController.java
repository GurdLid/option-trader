package com.registrationlogindemo.controller;

import com.registrationlogindemo.model.CustomUserDetails;
import com.registrationlogindemo.model.User;
import com.registrationlogindemo.service.OptionService;
import com.registrationlogindemo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@CrossOrigin
public class AdminController {
    /**
     * This is the controller for the admin pages. Currently, there is only the admin page to display all non admin users
     */
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
                        .getPrincipal(); //Getting the details of the current logged in user

        model.addAttribute("userdetails",userDetails); //Adding the user details to the model for front end display purposes

        List<User> traders = userService.getAllUsers(); //Getting all traders
        User admin = userService.findUserByEmail("admin@ot.com"); //removing the only admin from the list
        traders.remove(admin);
        model.addAttribute("traders", traders);

        return "admin";
    }


    @GetMapping("deleteUser") //Method to delete a non admin user
    public String deleteUser(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id")); //getting the correct user
        userService.deleteUser(id);

        return "redirect:/admin"; //refreshing the page after deletion
    }



}