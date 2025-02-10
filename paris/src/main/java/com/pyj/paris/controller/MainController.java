package com.pyj.paris.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping({"/", "/main.do"})
    public String home() {
        return "main";

    }
}