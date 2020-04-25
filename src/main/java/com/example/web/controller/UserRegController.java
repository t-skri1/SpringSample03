package com.example.web.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.persistence.entity.UserInfo;
import com.example.service.AuthService;
import com.example.web.form.AuthForm;

@Controller
@RequestMapping("/userReg")
public class UserRegController {
    @Autowired
    AuthService authService;

    final static Map<String, String> CHECK_ITEMS =
            Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
                private static final long serialVersionUID = 1L;

                {
                    put("ロール1",       "ROLE_Role1");
                    put("ロール2",       "ROLE_Role2");
                    put("ロール3",       "ROLE_Role3");
                }
            });

    @GetMapping("/index")
    public String index(Model model) {
        List<UserInfo> userInfoList = authService.findUserAll();
        model.addAttribute("userInfoList", userInfoList);
        return "userReg/index";
    }

    @GetMapping("/registered")
    public String registeredGet() {
        return "userReg/registered";
    }

    @GetMapping("/insert")
    public String insertGet(Model model) {
        model.addAttribute("authForm", new AuthForm());
        model.addAttribute("checkItems", CHECK_ITEMS);
        return "userReg/insert";
    }

    @PostMapping("/insert")
    public String insertPost(Model model, RedirectAttributes attributes,
            @Valid AuthForm authForm, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("checkItems", CHECK_ITEMS);
            model.addAttribute("signupError", true);
            return "userReg/insert";
        }
        try {
            authService.insertUser(authForm);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("checkItems", CHECK_ITEMS);
            model.addAttribute("signupError", true);
            return "userReg/insert";
        }

        attributes.addFlashAttribute("message", "登録しました。");
        return "redirect:/userReg/registered";
    }

    @GetMapping("/{id}/update")
    public String updateGet(Model model, @PathVariable("id") String id) {
        AuthForm authForm = authService.userRegById(id);

        model.addAttribute("authForm", authForm);
        model.addAttribute("checkItems", CHECK_ITEMS);
        return "userReg/update";
    }

    @PostMapping("/{id}/update")
    public String updatePost(Model model,
            RedirectAttributes attributes,
            @Valid AuthForm authForm, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("checkItems", CHECK_ITEMS);
            model.addAttribute("signupError", true);
            return "userReg/update";
        }
        try {
            authService.updateUser(authForm);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("checkItems", CHECK_ITEMS);
            model.addAttribute("signupError", true);
            return "userReg/update";
        }

        attributes.addFlashAttribute("message", "変更しました。");
        return "redirect:/userReg/registered";
    }

    @GetMapping("/{id}/delete")
    public String deleteGet(Model model, @PathVariable("id") String id) {
        AuthForm authForm = authService.userRegById(id);

        model.addAttribute("authForm", authForm);
        model.addAttribute("checkItems", Arrays.asList(authForm.getAuthority()));
        return "userReg/delete";
    }

    @PostMapping("/{id}/delete")
    public String deletePost(Model model, @PathVariable("id") String id,
            RedirectAttributes attributes,
            @Valid AuthForm authForm, BindingResult bindingResult, HttpServletRequest request) {

        try {
            authService.deleteUser(authForm.getUserId());
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("signupError", true);
            return "userReg/delete";
        }

        attributes.addFlashAttribute("message", "削除しました。");
        return "redirect:/userReg/registered";
    }
}