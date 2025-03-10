package com.pyj.paris.controller;

import com.pyj.paris.dto.ReservationDto;
import com.pyj.paris.dto.UserDto;
import com.pyj.paris.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@RequiredArgsConstructor
@RequestMapping("/reservation")
@Controller
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/request")
    public String requestReservation(@RequestParam("reservationDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date reservationDate,
                                     HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        String contents = request.getParameter("contents");

        if (user == null) {
            return "redirect:/user/login.form";
        }

        ReservationDto reservation = ReservationDto.builder()
                .userNo(user.getUserNo())
                .reservationDate(reservationDate)
                .contents(contents)
                .status("PENDING")
                .build();

        reservationService.requestReservation(reservation);

        return "redirect:/reservation/user";
    }

    @PostMapping("/approve")
    public String approveReservation(@RequestParam("reservationNo") int reservationNo) {
        reservationService.approveReservation(reservationNo);
        return "redirect:/reservation/pending";
    }

    @PostMapping("/reject")
    public String rejectReservation(@RequestParam("reservationNo") int reservationNo) {
        reservationService.rejectReservation(reservationNo);
        return "redirect:/reservation/pending";
    }

    @GetMapping("/pending")
    public String getPendingReservations(Model model) {
        model.addAttribute("pendingReservations", reservationService.getPendingReservations());
        model.addAttribute("allReservations", reservationService.getAllReservations());
        return "reservation/pending";
    }

    @GetMapping("/user")
    public String getUserReservations(HttpServletRequest request, Model model) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/user/login.form";
        }
        model.addAttribute("reservations", reservationService.getUserReservations(user.getUserNo()));
        return "reservation/user";
    }

}
