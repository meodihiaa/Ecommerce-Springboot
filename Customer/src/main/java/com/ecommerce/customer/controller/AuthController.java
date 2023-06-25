package com.ecommerce.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title","Đăng nhập");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("title", "Đăng ký");
        return "register";
    }
}
