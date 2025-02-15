package com.pyj.paris.controller;

import com.pyj.paris.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/login.form")
    public String loginForm(HttpServletRequest request, Model model) throws Exception {
        // referer : 이전 주소가 저장되는 요청 Header 값
        String referer = request.getHeader("referer");

        String redirectUrl = (referer != null) ? referer : request.getContextPath() + "/main";

        model.addAttribute("redirectUrl", redirectUrl);

        return "user/login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        userService.login(request, response);
        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        userService.logout(request, response);
        return null;
    }

}
