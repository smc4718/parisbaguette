package com.pyj.paris.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping({"/", "/main"})
    public String home(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login.form";
        }
        return "main";
    }
}