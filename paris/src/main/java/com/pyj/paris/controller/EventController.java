package com.pyj.paris.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/event")
@RequiredArgsConstructor
@Controller
public class EventController {

    @GetMapping("/list")
    public String list() {
        return "event/list";
    }
}
