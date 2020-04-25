package com.example.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.service.AuthService;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @GetMapping("/login")
    public String loginGet(Model model) {

        if(authService.adminCheck() <= 0) {
            return "redirect:/adminReg/index";
        }
        return "auth/login";
    }

    @GetMapping("/403")
    public String index403(Model model) {
        return "auth/403";
    }

    @GetMapping("/login-error")
    public String loginErrorGet(Model model) {
        model.addAttribute("loginError", true);
        return "auth/login";
    }
}