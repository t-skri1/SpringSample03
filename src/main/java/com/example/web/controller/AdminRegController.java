package com.example.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.service.AuthService;
import com.example.web.form.AuthForm;

@Controller
@RequestMapping("/adminReg")
public class AdminRegController {
    @Autowired
    AuthService authService;

    @GetMapping("/index")
    public String indexGet(Model model) {
        //ADMINロールユーザーは1つのみ
        if(authService.adminCheck() > 0) {
            return "redirect:registered";
        }

        model.addAttribute("authForm", new AuthForm());
        return "adminReg/index";
    }

    @PostMapping("/index")
    public String indexPost(Model model,
            @Valid AuthForm authForm, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("signupError", true);
            return "adminReg/index";
        }

        try {
            authService.insertAdmin(authForm);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("signupError", true);
            return "adminReg/index";
        }
        return "adminReg/done";
    }

    @GetMapping("/registered")
    public String registered(Model model) {
        return "adminReg/registered";
    }
}