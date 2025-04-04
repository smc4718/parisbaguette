package com.pyj.paris.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/event")
public class EventHomeController {

    @GetMapping("/list")
    public String eventHome() {
        return "event/list";
    }
}
