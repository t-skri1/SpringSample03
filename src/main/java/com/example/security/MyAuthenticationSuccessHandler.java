package com.example.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler  {
    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                throws IOException, ServletException {

        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

        //認証後のリダイレクト先
        if (authentication.getAuthorities().toString().contains("ADMIN")) {
            // ADMINロールはユーザー登録ページを表示する。
            redirectStrategy.sendRedirect(request, response, "/userReg/index");
        } else {
            // それ以外のロールの時に表示するページ。
            redirectStrategy.sendRedirect(request, response, "/hello/index");
        }
    }
}