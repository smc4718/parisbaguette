package com.pyj.paris.controller;

import com.pyj.paris.dto.ReservationDto;
import com.pyj.paris.dto.UserDto;
import com.pyj.paris.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

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
    @ResponseBody
    public Map<String, String> approveReservation(@RequestParam("reservationNo") int reservationNo) {
        reservationService.approveReservation(reservationNo);
        Map<String, String> response = new HashMap<>();
        response.put("status", "approved");
        response.put("message", "예약이 승인되었습니다.");
        return response;
    }

    @PostMapping("/reject")
    @ResponseBody
    public Map<String, String> rejectReservation(@RequestParam("reservationNo") int reservationNo) {
        reservationService.rejectReservation(reservationNo);
        Map<String, String> response = new HashMap<>();
        response.put("status", "rejected");
        response.put("message", "예약이 거절되었습니다.");
        return response;
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

    @GetMapping("/list")
    @ResponseBody
    public Map<String, List<ReservationDto>> getReservationsByMonth(@RequestParam("year") int year,
                                                                    @RequestParam("month") int month) {
        List<ReservationDto> reservations = reservationService.getReservationsByMonth(year, month);
        Map<String, List<ReservationDto>> reservationMap = new HashMap<>();

        for (ReservationDto reservation : reservations) {
            String dateKey = new SimpleDateFormat("yyyy-MM-dd").format(reservation.getReservationDate());
            reservationMap.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(reservation);
        }

        return reservationMap;
    }

}
