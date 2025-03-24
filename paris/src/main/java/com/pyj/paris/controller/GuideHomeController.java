package com.pyj.paris.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/guide")
@Controller
public class GuideHomeController {

    @GetMapping("/guideHome")
    public String guideHome() {
        return "guide/list"; // HTML 페이지 렌더링
    }
}